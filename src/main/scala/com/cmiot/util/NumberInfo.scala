package com.cmiot.util

import com.cmiot.tools.{Schema, DateTimeTool, Context}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

/**
  * Created by yxj on 2017/3/28.
  */
object NumberInfo {

  //  val conf = DBTool.getTable("(select PARAMVALUE from SA_DB_SYSPARAM where PARAMID = 'NUM_SECTION_CONFIG') t")
  //  val range = conf.first().get(0).toString.split("\\|")



  //@transient private val range = saDbSysParam.filter("paramid = 'NUM_SECTION_CONFIG'").selectExpr("paramvalue").first().getString(0).split("\\|")
  @transient private val range = Array("147", "10648", "184", "178", "172", "10647","14400")
  val msisdnRange = org.apache.spark.sql.functions.udf((msisdn: String) => {
    var result = "0"
    for (r <- range) {
      if (msisdn.startsWith(r)) {
        result = r
      }
    }
    result
  })

  def getAllMsisdnRangeImp = {
    val rddSmsFlow = Context.getSparkContext.parallelize(range).map(r => Row(r))
    Context.getSqlContext.createDataFrame(rddSmsFlow, StructType(Seq(StructField("RANGE", StringType))))
  }


  def getTitle(tableName: String, statsDate: String) = {
    tableName match {
      case "STATS_SUBS_ACTIVE_REGION_MONTH" => generateTitle(tableName, Constant.ACTIVEITEM,Constant.ACTIVEITEMCN,statsDate)
      case "STATS_PROVSUBSCRIBER_DAY" => generateTitle(tableName, Constant.USERITEM,Constant.USERITEMCN,statsDate)
      case "STATS_PROVSUBSCRIBER_WEEK" => generateTitle(tableName,Constant.USERITEM,Constant.USERITEMCN,statsDate)
      case "STATS_SUBSCRIBER_DAY" => generateTitle(tableName,Constant.USERITEM,Constant.USERITEMCN,statsDate)
      case "STATS_SUBS_ONLINE_MONTH" => generateTitle(tableName, Constant.ONLINEITEM,Constant.ONLINEITEMCN,statsDate)
      case _ => null
    }

  }

  def generateTitle(tableName: String, item: String, itemCN: String, statsDate: String)={
    val date = DateTimeTool.toDate(statsDate)
    val rdd = Context.getSparkContext.parallelize(range).map(r=>
      Row(tableName, date, r, r+itemCN.replace(",",","+r),range.indexOf(r)+1,"N"+r+"_"+item.replace(",",",N"+r+"_"),DateTimeTool.now)
    )
    Context.getSqlContext.createDataFrame(rdd,Schema.STATS_SEGMENT_QUERY_INFO)
  }

  // add by tongs 20170921 start
  val statsdatePstatsdate = org.apache.spark.sql.functions.udf((statsdate: String) => {
    val result = statsdate.substring(0, 10)
    result
  })
  // add by tongs 20170921 end

}