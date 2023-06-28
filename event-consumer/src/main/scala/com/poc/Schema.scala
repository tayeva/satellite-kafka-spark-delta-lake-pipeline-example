package com.poc

import org.apache.spark.sql.types.{
  StructType,
  StructField,
  StringType,
  LongType,
  DoubleType
}

object Schema {
  val eventSchema = StructType(
    Seq(
      StructField("satelliteName", StringType, nullable = false),
      StructField("timestampNs", LongType, nullable = false),
      StructField("tempC", DoubleType, nullable = false),
      StructField("batteryChargePct", DoubleType, nullable = false),
      StructField("altitudeKm", DoubleType, nullable = false),
      StructField("sensor1", DoubleType, nullable = false),
      StructField("sensor2", DoubleType, nullable = false),
      StructField("sensor3", DoubleType, nullable = false),
      StructField("receivedTimestamp", LongType, nullable = false)
    )
  )
}
