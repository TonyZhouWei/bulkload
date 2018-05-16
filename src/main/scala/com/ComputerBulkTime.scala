package com

import java.net.URI
import java.text.SimpleDateFormat
import java.util.Date

import com.cmiot.WriteToHBaseForBL
import com.cmiot.bulkload.HBaseUtilForBL
import com.cmiot.bulkload.HBaseUtilForBL.logger
import com.typesafe.config.ConfigFactory
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, LocatedFileStatus, Path}
import org.apache.hadoop.hbase.client.{HTable, ResultScanner, Scan}
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter
import org.slf4j.LoggerFactory

object ComputerBulkTime {
  var logger = LoggerFactory.getLogger(ComputerBulkTime.getClass)

  def main(args: Array[String]) = {
    logger.info("version: 2.3")
    logger.info("come in: " + args(0))
    HBaseUtilForBL.setSysConfigMap("smsFlow")
    computeTime(args(0).toLong, args(1).toLong)
  }

  def computeTime(bulkLoadStartTimeStamp: Long, rddStartTimeStamp: Long) = {
    try {
      val startDateTime = this.getDateTime(bulkLoadStartTimeStamp)
      logger.info("++++++++++++ start time: {}; timestamp: {} +++++++++++++", startDateTime, bulkLoadStartTimeStamp)
      this.countTable("smsFlow")
      val conf = ConfigFactory.load("app.properties")
      val hdfsMaster = conf.getString("hdfs.master")
      val fs = FileSystem.get(new URI(hdfsMaster), new Configuration())

      val hbaseFilePath = "/hbase/data/default/smsFlow/"
      val path = new Path(hbaseFilePath)
      val listFiles = fs.listFiles(path, true)
      while (listFiles.hasNext) {
        val file = listFiles.next()
        logger.info("file info: " + file.getPath)
        val fileName = file.getPath.getName
        if (fileName.contains("_SeqId_")) {
          val familyFolder = file.getPath.getParent
          val endTimeStamp = fs.getFileStatus(familyFolder).getModificationTime
          val modifyDateTime = getDateTime(endTimeStamp)
          val bulkLoadTimeSpan = endTimeStamp - bulkLoadStartTimeStamp
          val rddTimeSpan = endTimeStamp - rddStartTimeStamp
          logger.info("*********** familyName is: {}; rddTimeSpan is: {}; bulkLoadTimeSpan is: {}; HBase File is: {}; datetime is: {} **************",
            familyFolder.getName, rddTimeSpan.toString, bulkLoadTimeSpan.toString, file.getPath.toString, modifyDateTime.toString)
        }
      }
    } catch {
      case cause: Throwable => cause.printStackTrace(System.out)
    }
  }

  private def getDateTime(endTimeStamp: Long) = {
    val myformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    val time = new Date(endTimeStamp)
    val dateTime = myformat.format(time)
    dateTime
  }

  //清空表的数据
  def countTable(tableName: String) {
    logger.info("++++++++++++ start get the total count... +++++++++++++")
    val conf = HBaseUtilForBL.getHBaseConfig()
    val table = new HTable(conf, tableName);
    val scan = new Scan();
    scan.setFilter(new FirstKeyOnlyFilter());
    val resultScanner: ResultScanner = table.getScanner(scan);
    val it = resultScanner.iterator()
    var rowCount = 0
    while (it.hasNext) {
      rowCount += it.next().size()
    }

    logger.info("++++++++++++ total count is: " + rowCount)
  }
}
