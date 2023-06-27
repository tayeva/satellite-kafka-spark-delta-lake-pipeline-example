package com.poc

import java.nio.ByteBuffer
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.Row
import org.apache.spark.streaming.{StreamingContext, Seconds}
import org.apache.spark.streaming.dstream.InputDStream
import com.poc.Event
import com.poc.KafkaSparkClient
import com.poc.Utils
import com.poc.Schema

object EtlPipeline {

  def deserializeEvent(bytes: Array[Byte]): Event = {
    val buffer = ByteBuffer.wrap(bytes)
    val event = Event.getRootAsEvent(buffer);
    event
  }

  def createDeltaTable(spark: SparkSession, path: String): Unit = {
    val fs = FileSystem.get(new Configuration())
    val deltaTablePath = new Path(path)
    if (fs.exists(deltaTablePath)) {
      fs.delete(deltaTablePath, true)
    }
    val emptyDf: DataFrame = spark.createDataFrame(spark.sparkContext.emptyRDD[Row], Schema.eventSchema)
    emptyDf.write
      .format("delta")
      .save(path)
  }

  def getSparkConf(appName: String, sparkMaster: String): SparkConf = {
    val sparkConf = new SparkConf()
      .setAppName(appName)
      .setMaster(sparkMaster)
      .set("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
      .set("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
    sparkConf
  }

  def processKafkaStream(stream: InputDStream[ConsumerRecord[String,Array[Byte]]], spark: SparkSession, deltaTable: String): Unit = {
    stream.foreachRDD { rdd =>
      val events = rdd.map(record => {
        val event = deserializeEvent(record.value())
        val receivedTimestamp = record.timestamp()
        (event, receivedTimestamp)
      })
      val eventFields = events.map {case (event, receivedTimestamp) => 
        (
          event.satelliteName(), event.timestampNs(), event.tempC(),
          event.batteryChargePct(), event.altitudeKm(), event.sensor1(),
          event.sensor2(), event.sensor3(), receivedTimestamp
        )
      }
      val eventRows = eventFields.map {
        case (satelliteName, timestampNs, tempC, batteryChargePct, altitudeKm, sensor1, sensor2, sensor3, kafkaTimestamp) =>
          Row(satelliteName, timestampNs, tempC, batteryChargePct, altitudeKm, sensor1, sensor2, sensor3, kafkaTimestamp)
      }
      val eventsDf = spark.createDataFrame(eventRows, Schema.eventSchema)
      if (!eventsDf.isEmpty) {
        eventsDf.show()
        eventsDf.write
          .format("delta")
          .mode("append")
          .save(deltaTable)
      }
    }
  }
}

object Main {
  def main(args: Array[String]) : Unit = {
    if (args.length != 1) {
      println("Please provide the path to the properties file.")
      return
    } 
    val configFile = args(0)
    val properties = Utils.loadConfig(configFile)
    val client = new KafkaSparkClient(properties)
    val deltaTableProperty = properties.getProperty("deltaTable")
    val deltaTable = "/app/data/delta-tables/" + deltaTableProperty
    val appName = properties.getProperty("appName")
    val sparkMaster = properties.getProperty("sparkMaster")
    
    val sparkConf = EtlPipeline.getSparkConf(client.appName, client.sparkMaster)
    val streamingContext = new StreamingContext(sparkConf, Seconds(client.batchWindowSeconds))
    val spark = SparkSession.builder.config(sparkConf).getOrCreate()
    import spark.implicits._

    EtlPipeline.createDeltaTable(spark, deltaTable)

    val kafkaStream = client.getKafkaStream(streamingContext)
    EtlPipeline.processKafkaStream(kafkaStream, spark, deltaTable)

    streamingContext.start()
    streamingContext.awaitTermination()
  }
}