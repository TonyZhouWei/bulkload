package com.cmiot.bulkload

import com.cmiot.tools.Context
import com.typesafe.config.ConfigFactory
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.hbase.client.{ConnectionFactory, HBaseAdmin}
import org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles
import org.apache.hadoop.hbase.spark._
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.spark.sql.DataFrame

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import org.apache.hadoop.hbase.spark.HBaseRDDFunctions._
import java.net.URI

import org.slf4j.LoggerFactory

object HBaseUtilForBL {
  val logger = LoggerFactory.getLogger(getClass)

  def writeData(df: DataFrame, tableName: String, familyKeys: mutable.Map[String, String], familyColumns: Array[String], isClear: Boolean): Unit = {
    // 获取数据源RDD
    //val rdd: RDD[(String, String, String)] = getRDD(df)
    // 初始化系统配置信息
    this.setSysConfigMap(tableName)
    val outputPath = Config.Output_Path

    val startTime = System.currentTimeMillis();
    // 生成HFile
    generateHFile(df, tableName, outputPath, familyKeys, familyColumns)
    val midTime = System.currentTimeMillis();
    val spanTime = midTime - startTime;
    println("***************************HBaseBulkLoader.bulkLoad.end. SpanTime: " + spanTime + "**************************")

    // 将产生的hfile导入hbase
    val config: Configuration = getHBaseConfig()
    val conn = ConnectionFactory.createConnection(config)
    val table = conn.getTable(TableName.valueOf(tableName))
    val load = new LoadIncrementalHFiles(config)
    if (isClear) {
      //清空表的数据
      truncateTable(tableName)
    }

    load.doBulkLoad(new Path(outputPath), conn.getAdmin, table, conn.getRegionLocator(TableName.valueOf(tableName)))
    val endTime = System.currentTimeMillis()
    val midSpan = endTime - midTime
    val totalSpan = endTime - startTime
    logger.info("******************************End of all. midSpan: %d; total Time: %d******************************".format(midSpan, totalSpan))
  }

  /**
    * // 生成HFile
    *
    * @param rdd
    * @param tableName
    * @param outputPath
    */
  private def generateHFile(df: DataFrame, tableName: String, outputPath: String, familyKey: mutable.Map[String, String], familyColumns: Array[String]) = {
    println("**************before HBaseBulkLoader(conf).bulkLoad************************")
    val sc = Context.getSparkContext
    val config = this.getHBaseConfig()
    val rddSchema = df.schema.fieldNames
    val hbaseContext = new HBaseContext(sc, config)
    var fs = FileSystem.get(new URI(Config.HDFS_MASTER), new Configuration())
    var hdfsPath = new Path(outputPath)
    if (fs.exists(hdfsPath)) {
      fs.delete(hdfsPath, true)
      logger.info("++++++++++++++ file exits, deleted +++++++++++++++++++++")
    }

    df.rdd.hbaseBulkLoadThinRows(hbaseContext,
      TableName.valueOf(tableName),
      row => {
        var rowkey = row(0).toString()
        val familyQualifiersValues = new FamiliesQualifiersValues
        println("++++++++++++++rowKey: " + rowkey)
        for (i <- 1 until row.length) {
          if (row(i) != null) {
            val qualifier = rddSchema(i).toString
            val cf = familyKey.apply(qualifier)
            val value = row(i).toString()
            println("++++++++++++++cf: %s, fieldName: %s; value: %s".format(cf, qualifier, value))

            familyQualifiersValues += (Bytes.toBytes(cf), Bytes.toBytes(qualifier), Bytes.toBytes(value))
          }
        }

        (new ByteArrayWrapper(Bytes.toBytes(rowkey)), familyQualifiersValues)
      },
      outputPath,
      new java.util.HashMap[Array[Byte], FamilyHFileWriteOptions],
      compactionExclude = false)
  }

  /**
    * 获取数据源
    *
    * @param args
    * @return
    */
  private def getRDD(df: DataFrame) = {
    val rddSchema = df.schema.fieldNames
    var rddMedia = df.rdd.map(row => {
      var data = new ListBuffer[(String, String, String)]
      var rowkey = row(0).toString()
      println("++++++++++++++rowKey: " + rowkey)
      for (i <- 1 until row.length) {
        if (row(i) != null) {
          val fieldName = rddSchema(i).toString
          val value = row(i).toString()
          println("++++++++++++++fieldName: %s; value: %s".format(fieldName, value))
          data.append((rowkey, fieldName, value))
        }
      }

      data
    })

    var rdd = rddMedia.flatMap(x => x.iterator)
    println("+++++++++++++++++++++++++++get 3 tuple rdd, count: %s".format(rdd.count()))

    rdd
  }

  def setSysConfigMap(tableName: String) = {
    val hbaseConfig = ConfigFactory.load("writeToHBase.properties")

    Config.HBase_ClientPort = hbaseConfig.getString("hbase.zookeeper.property.clientPort")
    Config.HBase_Parent = hbaseConfig.getString("zookeeper.znode.parent")
    Config.HBase_Quorum = hbaseConfig.getString("hbase.zookeeper.quorum")
    Config.HBase_TableName = tableName
    Config.HBase_ColumnFamily = hbaseConfig.getString("hbase.columnfamily")
    Config.Output_Path = hbaseConfig.getString("output.path")
    Config.HDFS_MASTER = hbaseConfig.getString("hdfs.master")
  }

  def getHBaseConfig() = {
    val hbaseConfig = HBaseConfiguration.create()
    hbaseConfig.set("hbase.zookeeper.property.clientPort", Config.HBase_ClientPort)
    hbaseConfig.set("zookeeper.znode.parent", Config.HBase_Parent)
    hbaseConfig.set("hbase.zookeeper.quorum", Config.HBase_Quorum)

    hbaseConfig
  }

  //清空表的数据
  def truncateTable(tableName: String) {
    val conf = HBaseUtilForBL.getHBaseConfig()
    val hBaseAdmin = new HBaseAdmin(conf)
    val td = hBaseAdmin.getTableDescriptor(Bytes.toBytes(tableName))

    hBaseAdmin.disableTable(tableName)
    hBaseAdmin.deleteTable(tableName)
    hBaseAdmin.createTable(td)
    hBaseAdmin.close()
  }
}
