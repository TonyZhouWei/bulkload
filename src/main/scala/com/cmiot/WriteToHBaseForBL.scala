package com.cmiot

import com.cmiot.bulkload.HBaseUtilForBL
import com.cmiot.tools.{Context, DateTimeTool, FileTool}
import com.typesafe.config.ConfigFactory
import org.apache.hadoop.util.StringUtils
import org.slf4j.LoggerFactory

import scala.collection.mutable

/**
  * Created by yxj on 2017/7/25.
  */
//spark-submit --class com.cmiot.WriteToHbase target/stat-1.0-SNAPSHOT-jar-with-dependencies.jar -f tableName | -d tableName filename familyKeyMap rowKeyStr isClear
// -d 数据日期 -fn 文件名称* -t 表名称 -h htable名称  -rk rowkey* -fk 列簇对应关系* -cs rowkey连接字符串 -c 是否清空表 -p文件路径
object WriteToHBaseForBL extends App {

  val logger = LoggerFactory.getLogger(getClass)
  val executeMap = getParam(args)
  for (map <- executeMap) {
    if (!map.isEmpty) {
      try {
        write(map)
      } catch {
        case cause: Throwable => cause.printStackTrace(System.out)
      }
    }
  }

  def write(map: mutable.Map[String, String]) {
    //获取表信息
    val fileName = map.apply("fileName")
    val dataDate = map.apply("datadate")
    val rowKeyStr = map.apply("rowKeyStr")
    val concateStr = map.apply("concateStr")
    val familyMap = map.apply("familyMap")
    val isClear = map.apply("isClear").toBoolean
    val path = map.apply("path")

    val familyMaps = getFamilyMaps(familyMap)
    val familyColumns = getColumnFamilies(familyMap)
    //将dataFrame 以 指定的形式组成输出
    val data = getDataSource(path, fileName, dataDate, rowKeyStr, concateStr)
    HBaseUtilForBL.writeData(data, fileName, familyMaps, familyColumns, isClear)
  }

  def isExecute(tableName: String, table: String) = {
    tableName.equals(table)
  }

  def getFamilyMaps(familyMap: String) = {
    val map = mutable.Map.empty[String, String]
    for (kvs <- familyMap.split("\\|")) {
      val kv = kvs.split(":")
      val value = kv(0)
      for (key: String <- kv(1).split(",")) {
        map(key) = value
      }
    }
    map
  }

  def getColumnFamilies(familyMap: String) = {
    val ret = familyMap.split("\\|").map(kvs => {
      val kv = kvs.split(":")
      println("++++++++++++++++++ cloumnFamily: " + kv(0))
      kv(0)
    })

    ret
  }

  def getDataSource(path: String, filename: String, dataDate: String, rowKeyStr: String, concateStr: String) = {
    val sqlConetxt = Context.getSqlContext
    //获取路径信息

    val fullData = FileTool.loadParquetFile(path + filename)

    fullData.selectExpr()
    val tmpTable = getTempTableName(filename)
    fullData.createOrReplaceTempView(tmpTable)

    var dateColumn = ""
    if (fullData.schema.fieldNames.contains("P_STATDATE")) {
      dateColumn = "P_STATDATE"
    } else if (fullData.schema.fieldNames.contains("STATSDATE"))
      dateColumn = "STATSDATE"
    else {
      dateColumn = "STATDATE"
    }

    val queryCols = (StringUtils.join(",", fullData.schema.fieldNames) + ",")
    //.replace(rowKeyStr + ",", "")
    val queryStr = if (queryCols.endsWith(",")) queryCols.substring(0, queryCols.length - 1) else queryCols

    val key = "nvl(" + rowKeyStr.replace(",", ",''),'" + concateStr + "',nvl(") + ",'')"
    val sql = "select concat(" + key + ") as key," + queryStr +
      "  from " + tmpTable +
      "  where " + dateColumn + " = Cast('" + dataDate + "' as date)"
    println("++++++++++++++++++++++++sql: " + sql)

    val data = sqlConetxt.sql(sql).drop("filedate").drop("subdir")
    println("+++++++++++++++++++++++++data count: %s; data partition: %s".format(data.rdd.count(), data.rdd.getNumPartitions))
    data.printSchema()
    data.show()

    data
  }

  def getParam(param: Array[String]) = {
    val pathConf = ConfigFactory.load("app.properties")
    val outputPath = pathConf.getString("output.path")
    val dataPath = pathConf.getString("data.path")
    logger.info("outputPath: %s; dataPath: %s".format(outputPath, dataPath))
    val maps = mutable.Set(mutable.Map.empty[String, String])

    //-d 数据时间 -f 配置文件  -p 文件路径 -fn 文件名称 -fk 列簇对应关系 -c 是否清空
    val datadate = if (param.contains("-d")) DateTimeTool.toDate(param(param.indexOf("-d") + 1)).toString else DateTimeTool.today.toString
    if (param.contains("-f")) {
      val tables = param(param.indexOf("-f") + 1)
      for (tableName <- tables.split(",")) {
        // TODO there before is outputpath
        maps.add(getTbaleConfig(tableName, datadate, dataPath))
      }
    } else {
      val map = mutable.Map.empty[String, String]
      map("datadate") = datadate
      if (param.contains("-fn")) map("fileName") = param(param.indexOf("-fn") + 1)
      else {
        logger.error("need to specify fileName with tag -fn")
        System.exit(0)
      }
      map("table") = if (param.contains("-t")) param(param.indexOf("-t") + 1) else map.apply("fileName")
      map("htable") = if (param.contains("-h")) param(param.indexOf("-h") + 1) else map.apply("fileName")
      if (param.contains("-rk")) map("rowKeyStr") = param(param.indexOf("-rk") + 1)
      else {
        logger.error("need to specify the rowKey column with tag -rk")
        System.exit(0)
      }
      map("concateStr") = if (param.contains("-cs")) param(param.indexOf("-cs") + 1) else ("|")
      map("isClear") = if (param.contains("-c")) param(param.indexOf("-c") + 1) else ("false")
      map("path") = if (param.contains("-p")) param(param.indexOf("-p") + 1) else (dataPath)
      if (param.contains("-fk")) map("familyMap") = param(param.indexOf("-fk") + 1)
      else {
        logger.error("need to specify the family map with tag -fk [ family1:col1,col2|family2:col3,col4|... ]")
        System.exit(0)
      }
      maps.add(map)
    }
    maps
  }

  def getTbaleConfig(tableName: String, dataDate: String, path: String) = {
    val map = mutable.Map.empty[String, String]
    //获取表信息
    val tableConf = ConfigFactory.load("writeToHBase.json")
    val tableInfos = tableConf.getObjectList("tables")

    for (i <- 0 until tableInfos.size()) {
      val tableInfo = tableInfos.get(i).toConfig
      val table = tableInfo.getString("tablename")
      logger.info("table Name: " + table)
      if (isExecute(tableName, table)) {
        logger.info("find the table: " + tableName)
        map("table") = table
        map("datadate") = dataDate
        map("htable") = if (tableInfo.hasPath("htable")) tableInfo.getString("htable") else table
        map("fileName") = if (tableInfo.hasPath("filename")) tableInfo.getString("filename") else table
        map("rowKeyStr") = tableInfo.getString("rowkeystr")
        map("concateStr") = if (tableInfo.hasPath("concatestr")) tableInfo.getString("concatestr") else "|"
        map("isClear") = if (tableInfo.hasPath("isclear")) tableInfo.getString("isclear") else "false"
        map("path") = path
        //将 字段与列簇 转为键值对应
        map("familyMap") = tableInfo.getString("familymap")
      }
    }
    map
  }

  def getTempTableName(fn: String) = {
    fn + System.currentTimeMillis().toString
  }

}