package com.poc

import java.util.Properties
import scala.io.Source

object Utils {
  def loadConfig(configFile: String): Properties = {
    val properties = new Properties()
    val file = Source.fromFile(configFile)
    properties.load(file.bufferedReader())
    file.close()
    properties
  }
}
