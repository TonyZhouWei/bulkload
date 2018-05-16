package com.test

import java.text.SimpleDateFormat
import java.util.Date

object MainTest {
  def main(args: Array[String]): Unit ={
    val myformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    val timestamp = 1509428864805L
    val time = new Date(timestamp)
    val dateTime = myformat.format(time)
    print(dateTime)
  }
}
