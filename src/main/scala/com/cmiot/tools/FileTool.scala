package com.cmiot.tools

import java.sql.Date

import com.cmiot.util.Constant
import com.typesafe.config.ConfigFactory
import org.apache.hadoop.io.SecureIOUtils.AlreadyExistsException
import org.apache.spark.SparkContext
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer

class FileTool private(sc: SparkContext, sqlContext: SQLContext) {
  @transient val logger = LoggerFactory.getLogger(getClass)

  @transient val conf = ConfigFactory.load("app.properties")
  @transient val path = conf.getString("data.path")
  @transient val pbossFiles = conf.getString("pboss.files")
  @transient val inPath = conf.getString("data.path")
  @transient val outPath = conf.getString("output.path")
  @transient val gprsFile = conf.getString("gprs.file")
  @transient val smsFile = conf.getString("sms.file")
  @transient val accountFile = conf.getString("account.file")
  @transient val subsSmsFile = conf.getString("subsSms.file")
  @transient val udmFile = conf.getString("udm.file")

  if (logger.isDebugEnabled) {
    logger.debug("data.path={}", inPath)
    logger.debug("pboss.files={}", pbossFiles)
    logger.debug("output.path={}", outPath)
    logger.debug("pboss.files={}", pbossFiles)
    logger.debug("gprs.file={}", gprsFile)
    logger.debug("sms.file={}", smsFile)
    logger.debug("account.file={}", accountFile)
    logger.debug("subsSms.file={}", subsSmsFile)
    logger.debug("udm.file={}", udmFile)
  }

  def getGprsFlow = {
    val rowRDD = loadTextFile(gprsFile).filter(_.size == 3)
      .map(r => Row(DateTimeTool.toDate(r(0)), r(1), r(2).toLong))
    sqlContext.createDataFrame(rowRDD, Schema.GPRS_FILE)
  }

  def getSmsFlow = {
    val rowRDD = loadTextFile(smsFile).filter(_.size == 4)
      .map(r => Row(DateTimeTool.toDate(r(0)), r(1), r(2).toLong, r(3).toLong))
    sqlContext.createDataFrame(rowRDD, Schema.SMS_FILE)
  }

  def getTestData(fileName: String) = {
    val rowRDD = loadTextFile(fileName, ",")
      .map(r => Row("1","ABCD","EFG","HIJK","LML","OPQLMN"))
    sqlContext.createDataFrame(rowRDD, Schema.TEST_CSV_FILE)
  }

  def loadTextFile(file: String, splitter: String = ";") = sc.textFile(file).map(_.split(splitter))

  def loadParquetDataFile(file: String) = {
    //println(file+"------------------------------------------------------------------------------------")
    println("select * from parquet.`" + inPath + file + ".parquet`")
    sqlContext.sql("select * from parquet.`" + inPath + file + ".parquet`")
  }

  def loadParquetFile(file: String) = sqlContext.sql("select * from parquet.`" + file + ".parquet`")

  def loadParquetPath(path: String) = sqlContext.read.option("mergeSchema", "true").parquet(path)

  def writeParquet(df: DataFrame, file: String, mode: String = "append"): Unit = {
    df.write.mode(mode).parquet(outPath + file + ".parquet")
  }

  def writeParquetSubDir(df: DataFrame, file: String, subDirectory: String, mode: String = "append"): Unit = {
    df.write.mode(mode).parquet(outPath + file + ".parquet/subdir=" + subDirectory)
  }

  def writeParquetPartStr(df: DataFrame, file: String, dateColumn: String, mode: String = "append"): Unit = {
    df.withColumn("filedate", df(dateColumn)).write.mode(mode)
      .partitionBy("filedate").parquet(outPath + file + ".parquet")
  }

  def writeParquetPart(df: DataFrame, file: String, dateColumn: String, mode: String = "append"): Unit = {
    df.withColumn("filedate", date_format(df(dateColumn), "yyyyMMdd")).write.mode(mode)
      .partitionBy("filedate").parquet(outPath + file + ".parquet")
  }

  def writeParquetPartDir(df: DataFrame, file: String, statDate: Date, dataType: String, subDirectory: String = "", mode: String = "append"): Unit = {
    val date = DateTimeTool.getDate(statDate.toString, "yyyy-MM-dd")
    val dataDate = dataType match {
      case Constant.DAY => date.minusDays(1).toString("yyyyMMdd")
      case Constant.WEEK => date.minusDays(7).toString("yyyyMMdd")
      case Constant.MONTH => date.minusDays(1).withDayOfMonth(1).toString("yyyyMMdd")
    }
    val subDir = subDirectory.length() match {
      case 0 => ""
      case _ => "/subdir=" + subDirectory
    }
    df.write.mode(mode).parquet(outPath + file + ".parquet/filedate=" + dataDate + subDir)
  }

  def loadXmlFile(file: String, tag: String) = sqlContext.read.format("xml").option("rowTag", tag).load(pbossFiles + file)

  def getAccountInfo = {
    val df = loadXmlFile(accountFile, "CardActInfo")
    df.select(df("MSISDN"), df("AccountInfo"), explode(df("ServDataAmount")) as "ServDataAmount")
      .select("MSISDN",
        "AccountInfo.UserStatus",
        "ServDataAmount.DataAmountType",
        "ServDataAmount.ProdInstID",
        "ServDataAmount.DataTotal",
        "ServDataAmount.DataAmount",
        "ServDataAmount.StartTime",
        "ServDataAmount.EndTime")
      .selectExpr(
        "CAST(MSISDN as string) as MSISDN",
        "CAST(CASE WHEN DataAmountType=1 THEN ProdInstID else null end as string) as SMSPRODINSTID",
        "CAST(CASE WHEN DataAmountType=2 THEN ProdInstID else null end as string) as GPRSPRODINSTID",
        "CAST(CASE WHEN DataAmountType=3 THEN ProdInstID else null end as string) as VOICEPRODINSTID",
        "CAST(CASE WHEN DataAmountType=1 THEN DataAmount else 0 end as long)  as SMSUSEDFLOW",
        "CAST(CASE WHEN DataAmountType=1 THEN DataTotal else 0 end as long)  as SMSTOTAL",
        "CAST(CASE WHEN DataAmountType=2 THEN DataAmount else 0 end as long)  as GPRSUSEDFLOW",
        "CAST(CASE WHEN DataAmountType=2 THEN DataTotal else 0 end as long)  as GPRSTOTAL",
        "CAST(CASE WHEN DataAmountType=3 THEN DataAmount else 0 end  as long) as VOICEUSEDFLOW",
        "CAST(CASE WHEN DataAmountType=3 THEN DataTotal else 0 end  as long) as VOICETOTAL",
        "cast(concat(substr(StartTime,1,4),'-',substr(StartTime,5,2),'-',substr(StartTime,7,2)) as date) as STARTTIME",
        "cast(concat(substr(EndTime,1,4),'-',substr(EndTime,5,2),'-',substr(EndTime,7,2)) as date) as ENDTIME",
        "cast('" + DateTimeTool.sysDateTime + "' as TIMESTAMP) as CREATEDATE"
      )
  }

  def getCmSubsSms = {
    val splitter = "\\|"
    val rowRDD = loadTextFile(subsSmsFile, splitter).filter(_.size == 20)
      .map(r => Row(r(0).trim, r(1).trim, r(2).trim, r(3).trim, r(4).trim, r(5).trim, r(6).trim, r(7).trim, r(8).trim,
        r(9).trim, r(10).trim, r(11).trim, r(12).trim, r(13).trim, r(14).trim, r(15).trim, r(16).trim, r(17).trim,
        DateTimeTool.toTimestamp(r(18)), DateTimeTool.toTimestamp(r(19))))
    sqlContext.createDataFrame(rowRDD, Schema.SUBS_SMS_FILE)
  }

  def getCmSubsTrafficDaily = {

    val splitter = "\\|"
    val rowRDD = sc.textFile(pbossFiles + udmFile).flatMap(line => {
      var lines = new ArrayBuffer[Row]
      if (!line.trim().isEmpty) {
        val cols = line.split(splitter)
        val msisdn = cols(0)
        val processTime = DateTimeTool.toDate(cols(1))
        val sms = cols(2).replaceAll("[\\}\\{]", "").split("\\,")
        val smsTotalUnits = sms(0).toLong
        val smsAcctPeriod = sms(1)
        val voice = cols(4).replaceAll("[\\}\\{]", "").split("\\,")
        val voiceTotalUnits = voice(0).toLong
        val dialingTotalUnits = voice(1).toLong
        val calledTotalUnits = voice(2).toLong
        val voiceAcctPeriod = voice(3)
        val gprses = cols(3).split("\\}\\,\\{")
        for (i <- 0 until gprses.length) {
          val gprs = gprses(i).replaceAll("[\\}\\{]", "").split("\\,")
          val apn = gprs(0)
          val gprsTotalUnits = gprs(1).toLong
          val gprsAcctPeriod = gprs(2)
          lines += Row(msisdn, processTime, smsTotalUnits, smsAcctPeriod, apn, gprsTotalUnits, gprsAcctPeriod, voiceTotalUnits, dialingTotalUnits, calledTotalUnits, voiceAcctPeriod)
        }
      }
      lines
    })
    sqlContext.createDataFrame(rowRDD, Schema.UDM_ACCT_TRAFFIC_DAILY)
  }

  def getUdmAcctTrafficDaily = {

    val splitter = "\\|"
    val rowRDD = sc.textFile(pbossFiles + udmFile).flatMap(line => {
      var lines = new ArrayBuffer[Row]
      if (!line.trim().isEmpty) {
        val cols = line.split(splitter)
        val msisdn = cols(0)
        val processTime = DateTimeTool.toDate(cols(1))
        val sms = cols(2).replaceAll("[\\}\\{]", "").split("\\,")
        val smsTotalUnits = sms(0).toLong
        val smsAcctPeriod = sms(1)
        val voice = cols(4).replaceAll("[\\}\\{]", "").split("\\,")
        val voiceTotalUnits = voice(0).toLong
        val dialingTotalUnits = voice(1).toLong
        val calledTotalUnits = voice(2).toLong
        val voiceAcctPeriod = voice(3)
        val gprses = cols(3).split("\\}\\,\\{")
        for (i <- 0 until gprses.length) {
          val gprs = gprses(i).replaceAll("[\\}\\{]", "").split("\\,")
          val apn = gprs(0)
          val gprsTotalUnits = gprs(1).toLong
          val gprsAcctPeriod = gprs(2)
          lines += Row(msisdn, processTime, smsTotalUnits, smsAcctPeriod, apn, gprsTotalUnits, gprsAcctPeriod, voiceTotalUnits, dialingTotalUnits, calledTotalUnits, voiceAcctPeriod)
        }
      }
      lines
    })
    sqlContext.createDataFrame(rowRDD, Schema.UDM_ACCT_TRAFFIC_DAILY)
  }

  /**
    * 将存储过程结果表写入pqrquet文件，并将分区字段与表关联
    *
    * @author liuwei
    * @param df
    * @param file
    * @param partitionKey
    * @param partitionValue
    */
  def writeStatResultParquet(df: DataFrame, file: String, partitionKey: String, partitionValue: String): Unit = {

    val table_prefix = "6v_"
    (partitionKey == null || partitionKey.isEmpty) match {
      case true =>
        df.write.mode("overwrite").parquet(outPath + file.toLowerCase + ".parquet")
      case false => {
        //删除原有分区字段
        val delStr = "alter table " + table_prefix + file.toLowerCase + " drop partition (" + partitionKey + "='" + partitionValue + "')"
        try {
          sqlContext.sql(delStr)
        }
        catch {
          case e: org.apache.spark.sql.AnalysisException => println(file + "表无法删除(" + partitionKey + "='" + partitionValue + "')分区")
        }
        //插入数据
        df.write.mode("overwrite").parquet(outPath + file.toLowerCase + ".parquet" + "/" + partitionKey + "=" + partitionValue)
        //添加分区字段
        val sqlStr = "alter table " + table_prefix + file.toLowerCase + " add partition (" + partitionKey + "='" + partitionValue + "') location '" + partitionKey + "=" + partitionValue + "'"
        try {
          sqlContext.sql(sqlStr)
        }
        catch {
          case e: org.apache.spark.sql.AnalysisException => println(file + "表无法关联(" + partitionKey + "='" + partitionValue + "')分区")

        }

      }
    }
  }


  /**
    * 获取指定的结果表的所有数据
    *
    * @param file
    * @return
    */
  def loadParquetStatsDataFile(file: String): DataFrame = {
    println("select * from parquet.`" + outPath + file.toLowerCase + ".parquet`--------------")
    sqlContext.sql("select * from parquet.`" + outPath + file.toLowerCase + ".parquet`")
  }
}

case object FileTool extends FileTool(Context.getSparkContext, Context.getSqlContext)
