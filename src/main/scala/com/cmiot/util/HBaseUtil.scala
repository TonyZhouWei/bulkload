package com.cmiot.util

import java.io.PrintWriter

import com.typesafe.config.ConfigFactory
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{HBaseAdmin, HTable, Put}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.sql.DataFrame

import scala.collection.mutable

/**
  * Created by yxj on 2017/7/25.
  */
object HBaseUtil extends Serializable {
  def getConfig(tableName: String = null) = {
    val hbaseConfig = ConfigFactory.load("hbase.properties")
    val prod = hbaseConfig.getString("hbase.zookeeper.property.clientPort")
    val parent = hbaseConfig.getString("zookeeper.znode.parent")
    val quorum = hbaseConfig.getString("hbase.zookeeper.quorum")
    //    val core_site = hbaseConfig.getString("core.site")
    //    val hbase_site = hbaseConfig.getString("hbase.site")

    val config = HBaseConfiguration.create()
    config.set("hbase.zookeeper.property.clientPort", prod)
    config.set("zookeeper.znode.parent", parent)
    config.set("hbase.zookeeper.quorum", quorum)
    //    config.addResource(new Path(core_site))
    //    config.addResource(new Path(hbase_site))
    if (!(tableName == null)) {
      config.set(TableOutputFormat.OUTPUT_TABLE, tableName)
    }
    config
  }

  //将dataFrame的数据写入到Hbase
  def writeData(dt: DataFrame, tableName: String, familyKey: mutable.Map[String, String], isClear: Boolean) {

    if (isClear) {
      truncateTable(tableName)
    }

    val rddSchema = dt.schema.fieldNames

    val conf = getConfig(tableName)

    println("````````````````````conf: " + conf.get("zookeeper.znode.parent") + "``````````````````````````````````" )

    val job = new Job(conf)
    println("````````````````````dt.rdd.count: " + dt.rdd.count() + "``````````````````````````````````" )
    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setOutputValueClass(classOf[Put])
    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])

    dt.rdd.map(r => {
      val key = r(0).toString
      //val key = "r5"
      val out=new PrintWriter("/opt/aa.txt")
      out.println("key: ")
      out.println(key)

      val put = new Put(Bytes.toBytes(key))
      for (i <- 1 until r.length) {
        if (!(r(i) == null)) {
          //put.addColumn(Bytes.toBytes(familyKey), Bytes.toBytes(r.schema.fieldNames(i)), Bytes.toBytes(r.getString(i)))
          val column = rddSchema(i).toString

          out.println("column:")
          out.println(column)

          val value = r(i).toString

          out.println("value:")
          out.println(value)

          put.addColumn(Bytes.toBytes("info"), Bytes.toBytes(column), Bytes.toBytes(value))
          if (familyKey.contains("*")) {
            put.addColumn(Bytes.toBytes(familyKey.apply("*")), Bytes.toBytes(column), Bytes.toBytes(value))
          }
          if (familyKey.contains(column)) {
            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes(column), Bytes.toBytes(value))
          }
        }
      }

      out.flush()
      out.close()
      (new ImmutableBytesWritable(Bytes.toBytes(key)), put)
    }).saveAsNewAPIHadoopDataset(job.getConfiguration)
  }

  //清空表的数据
  def truncateTable(tableName: String) {
    val hBaseAdmin = new HBaseAdmin(getConfig())
    val td  = hBaseAdmin.getTableDescriptor(Bytes.toBytes(tableName))

    print("``````````````````````" + td)

    hBaseAdmin.disableTable(tableName)
    hBaseAdmin.deleteTable(tableName)
    hBaseAdmin.createTable(td)
    hBaseAdmin.close()
  }
}