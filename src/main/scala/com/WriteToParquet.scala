package com

import java.net.URI

import com.cmiot.WriteToHBaseForBL.getTempTableName
import com.cmiot.tools.{Context, FileTool}
import com.typesafe.config.ConfigFactory
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}

object WriteToParquet {
  def main(args: Array[String]) = {
    val path = if (args.length > 0) args(0) else ""
    generateData(path)
  }

  private def generateData(csvPath: String) = {
    val sc = Context.getSparkContext
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    println("csv path: " + csvPath)
    //    读取文件生成RDD
    val file = sc.textFile(csvPath)

    //定义parquet的schema，数据字段和数据类型需要和hive表中的字段和数据类型相同，否则hive表无法解析
    val schema = (new StructType)
      .add("MSISDN", StringType, true)
      .add("STATDATE", StringType, false)
      .add("OPRDATE", StringType, false)
      .add("SMSFLOW_STANDARD", StringType, false)
      .add("SMSFLOW_CUSTOMIZE", StringType, false)
      .add("P_STATDATE", StringType, false)

    val rowRDD = file.map(_.split(",")).map(p => Row(p(0), p(1), p(2), p(3), p(4), p(5)))
    //    将RDD装换成DataFrame
    val peopleDataFrame = sqlContext.createDataFrame(rowRDD, schema)
    peopleDataFrame.registerTempTable("test")

    val pathConf = ConfigFactory.load("app.properties")
    var hdfsMaster = pathConf.getString("hdfs.master")
    println("+++++++++ hdfsMaster: %s +++++++++", hdfsMaster)
    var parquetName = "smsFlow.parquet"
    var outputPath = pathConf.getString("data.path")
    var hdfsPath = new Path(outputPath + "/" + parquetName)
    println("+++++++++ hdfsPath: %s +++++++++", hdfsPath.toString)
    var hdfs = FileSystem.get(new URI(hdfsMaster), new Configuration)
    if (hdfs.exists(hdfsPath)) {
      hdfs.delete(hdfsPath, true)
      println("+++++ file exists, has deleted +++++++")
    }

    peopleDataFrame.write.parquet(hdfsPath.toString)

    val sqlConetxt = Context.getSqlContext
    //获取路径信息

    val loadPath = outputPath + "/" + parquetName.split("\\.")(0)
    val fullData = FileTool.loadParquetFile(loadPath)

    fullData.selectExpr()
    val tmpTable = getTempTableName(parquetName.split("\\.")(0))
    fullData.registerTempTable(tmpTable)

    val data = sqlConetxt.sql("select * from " + tmpTable).drop("filedate").drop("subdir")

    data.printSchema()
    data.show()
    println("+++++++++++++++TotalCount is: " + data.count() + "+++++++++++++++++++++++++")
  }
}
