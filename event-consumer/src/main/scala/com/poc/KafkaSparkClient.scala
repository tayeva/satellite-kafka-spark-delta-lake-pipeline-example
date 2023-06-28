package com.poc

import java.nio.ByteBuffer
import java.util.Properties
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.ByteArrayDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.StreamingContext
import scala.io.Source

class KafkaSparkClient(properties: Properties) {
  val appName: String = properties.getProperty("appName")
  val sparkMaster: String = properties.getProperty("sparkMaster")
  val kafkaTopics: Array[String] =
    properties.getProperty("kafkaTopics").split(",")
  val kafkaServers: String = properties.getProperty("kafkaServers")
  val kafkaGroupId: String = properties.getProperty("kafkaGroupId")
  val kafkaOffset: String = properties.getProperty("kafkaOffset")
  val batchWindowSeconds: Int =
    properties.getProperty("batchWindowSeconds").toInt

  def getKafkaParams(): Map[String, Object] = {
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> kafkaServers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[ByteArrayDeserializer],
      "group.id" -> kafkaGroupId,
      "auto.offset.reset" -> kafkaOffset,
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    kafkaParams
  }

  def getKafkaStream(
      context: StreamingContext
  ): InputDStream[ConsumerRecord[String, Array[Byte]]] = {
    val kafkaStream = KafkaUtils.createDirectStream[String, Array[Byte]](
      context,
      PreferConsistent,
      Subscribe[String, Array[Byte]](kafkaTopics, getKafkaParams())
    )
    kafkaStream
  }
}
