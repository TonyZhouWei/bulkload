package com.cmiot.tools

import org.apache.spark.sql.types.{StructField, _}

object Schema {
  val TEST_CSV_FILE = StructType(
    Seq(StructField("KEY", StringType),
      StructField("COL1", StringType),
      StructField("COL2", StringType),
      StructField("COL3", StringType),
      StructField("COL4", StringType),
      StructField("COL5", StringType)))


  val GPRS_FILE = StructType(
    Seq(StructField("MSISDN", StringType), StructField("STATDATE", DateType), StructField("OPRDATE", DateType), StructField("GPRSFLOW", LongType), StructField("P_STATDATE", StringType)))

  //缺省字段SMSFLOW_STANDARD
  val SMS_FILE = StructType(
    Seq(StructField("MSISDN", StringType), StructField("STATDATE", DateType), StructField("OPRDATE", DateType), StructField("SMSFLOW_STANDARD", LongType),
      StructField("SMSFLOW_CUSTOMIZE", LongType), StructField("P_STATDATE", StringType)))

  //全表字段,增加字段SMSFLOW_STANDARD
  val SMS_FILE_FIVE = StructType(
    Seq(StructField("STATDATE", DateType), StructField("MSISDN", StringType), StructField("SMSFLOWSTANDARD", LongType),
      StructField("SMSFLOW_CUSTOMIZE", LongType), StructField("SMSFLOW_STANDARD", LongType)))

  val GPRSDAY_FILE = StructType(
    Seq(StructField("MSISDN", StringType), StructField("STATDATE", DateType), StructField("OPRDATE", TimestampType),
      StructField("GPRSFLOW", LongType)))

  val SUBS_SMS_FILE = StructType(
    Seq(StructField("SMSSEQ", StringType), StructField("SMSTYPE", StringType), StructField("USERTYPE", StringType),
      StructField("MSISDN", StringType), StructField("ECID", StringType), StructField("THIRDPARTNUMBER", StringType),
      StructField("SERVCODE", StringType), StructField("BUSINESSCODE", StringType),
      StructField("CHARGINGTYPE", StringType), StructField("FUNCTIONCOST", StringType),
      StructField("MONTHCOST", StringType), StructField("SMSSENDSTATUS", StringType),
      StructField("SMSSENDPRIORITY", StringType), StructField("SMSLENGTH", StringType),
      StructField("MSISDNBELONG", StringType), StructField("GATEWAYCODE", StringType),
      StructField("FORWARDGATEWAYCODE", StringType), StructField("SMSCCODE", StringType),
      StructField("APPLYTIME", TimestampType), StructField("DEALENDTIME", TimestampType)))

  // add by tongs 20170914 start
  val SUBS_SMS = StructType(
    Seq(
      StructField("SMSSEQ", StringType),
      StructField("SMSTYPE", StringType),
      StructField("USERTYPE", StringType),
      StructField("MSISDN", StringType),
      StructField("SUBSID", LongType),
      StructField("CUSTID", StringType),
      StructField("PROVINCEID", StringType),
      StructField("THIRDPARTNUMBER", StringType),
      StructField("SERVCODE", StringType),
      StructField("BUSINESSCODE", StringType),
      StructField("CHARGINGTYPE", StringType),
      StructField("FUNCTIONCOST", StringType),
      StructField("MONTHCOST", StringType),
      StructField("SMSSENDSTATUS", StringType),
      StructField("SMSSENDPRIORITY", StringType),
      StructField("SMSLENGTH", StringType),
      StructField("MSISDNBELONG", StringType),
      StructField("GATEWAYCODE", StringType),
      StructField("FORWARDGATEWAYCODE", StringType),
      StructField("SMSCCODE", StringType),
      StructField("APPLYTIME", TimestampType),
      StructField("DEALENDTIME", TimestampType),
      StructField("P_DEALENDTIME", StringType)
    )
  )
  // add by tongs 20170914 end

  val PRODTOTAL_FILE = StructType(
    Seq(StructField("SUBSID", LongType),
      StructField("MSISDN", StringType),
      StructField("PROVINCEID", StringType),
      StructField("TOTALGPRS", LongType),
      StructField("TOTALSMS", LongType)
    ))

  // edit by tongs 20170919 start
  val STATS_SUBS_SIMFLOWDAY = StructType(
    Seq(
      StructField("PROVINCEID", StringType),
      StructField("MSISDN", StringType),
      StructField("CUSTID", StringType),
      StructField("SMSTOTAL", LongType),
      StructField("SMSUSEDFLOW", LongType),
      StructField("GPRSTOTAL", LongType),
      StructField("GPRSUSEDFLOW", LongType),
      StructField("VOICETOTAL", LongType),
      StructField("VOICEUSEDFLOW", LongType),
      StructField("GPRSYESTERDAYFLOW", LongType),
      StructField("SMSYESTERDAYFLOW", LongType),
      StructField("STATSDATE", TimestampType),
      StructField("P_STATSDATE", StringType)
    )
  )
  // edit by tongs 20170919 end

  val SUBSCRIBER = StructType(
    Seq(
      StructField("SUBSID", LongType),
      StructField("PROVINCEID", StringType),
      StructField("NETTYPE", StringType),
      StructField("MSISDN", StringType),
      StructField("IMSI", StringType),
      StructField("ICCID", StringType),
      StructField("STARTDATE", TimestampType),
      StructField("ENDDATE", TimestampType),
      StructField("STATUS", StringType),
      StructField("STATUSDATE", TimestampType),
      StructField("REGIONID", StringType),
      StructField("CREATEDATE", TimestampType),
      StructField("OPRSEQ", StringType),
      StructField("OPRTYPE", StringType),
      StructField("ECSUBSID", StringType),
      StructField("OPRTIME", TimestampType),
      StructField("INDUSTRYID", StringType),
      StructField("REMARK", StringType),
      StructField("OTHERSTATUS", StringType),
      StructField("NETSTATUS1", LongType),
      StructField("GPRSSTATUS1", StringType),
      StructField("MODIFYTIME", TimestampType),
      StructField("DETAILDESCRIBE", StringType),
      StructField("FUTURESTATUS", StringType),
      StructField("CUSTID", StringType),
      StructField("CUSTNAME", StringType),
      StructField("CUSTTYPE", IntegerType),
      StructField("DATASOURCE", StringType),
      StructField("ISSIGNGPRS", LongType),
      StructField("ISSIGNSMS", LongType),
      StructField("OPENTIME", TimestampType),
      StructField("USETIME", TimestampType),
      StructField("LOGICREGIONID", StringType),
      StructField("GROUPBELONG", StringType),
      StructField("BILLINGSTATUS", StringType),
      StructField("JOINECTIME", TimestampType),
      StructField("QUITECTIME", TimestampType),
      StructField("PREVIOUSECID", StringType),
      StructField("CARDTYPE", StringType),
      StructField("GPRSSTATUS1TIMESTAMP", LongType),
      StructField("ACTIVATIONTIME", TimestampType),
      StructField("GROUP_UPD_TIME", TimestampType)
    )
  )

  // edit by tongs 20170914 start
  val SUBSCRIBER_DISUSER = StructType(
    Seq(
      StructField("SUBSID", LongType),
      StructField("PROVINCEID", StringType),
      StructField("NETTYPE", StringType),
      StructField("MSISDN", StringType),
      StructField("IMSI", StringType),
      StructField("ICCID", StringType),
      StructField("STARTDATE", TimestampType),
      StructField("ENDDATE", TimestampType),
      StructField("STATUS", StringType),
      StructField("STATUSDATE", TimestampType),
      StructField("REGIONID", StringType),
      StructField("CREATEDATE", TimestampType),
      StructField("OPRSEQ", StringType),
      StructField("OPRTYPE", StringType),
      StructField("ECSUBSID", StringType),
      StructField("OPRTIME", TimestampType),
      StructField("INDUSTRYID", StringType),
      StructField("REMARK", StringType),
      StructField("OTHERSTATUS", StringType),
      StructField("NETSTATUS1", LongType),
      StructField("GPRSSTATUS1", StringType),
      StructField("MODIFYTIME", TimestampType),
      StructField("DETAILDESCRIBE", StringType),
      StructField("CUSTID", StringType),
      StructField("CUSTNAME", StringType),
      StructField("CUSTTYPE", LongType),
      StructField("DATASOURCE", StringType),
      StructField("ISSIGNGPRS", LongType),
      StructField("ISSIGNSMS", LongType),
      StructField("OPENTIME", TimestampType),
      StructField("USETIME", TimestampType),
      StructField("LOGICREGIONID", StringType),
      StructField("GROUPBELONG", StringType),
      StructField("CARDTYPE", StringType),
      StructField("B_TIME", TimestampType),
      StructField("B_REMARK", StringType)
    )
  )
  // edit by tongs 20170914 end

  val STATS_SUBS_SIM_FLOW_DAY = StructType(
    Seq(
      StructField("PROVINCEID", StringType),
      StructField("MSISDN", StringType),
      StructField("CUSTID", StringType),
      StructField("SMSTOTAL", LongType),
      StructField("SMSUSEDFLOW", LongType),
      StructField("GPRSTOTAL", LongType),
      StructField("GPRSUSEDFLOW", LongType),
      StructField("VOICETOTAL", LongType),
      StructField("VOICEUSEDFLOW", LongType),
      StructField("GPRSYESTERDAYFLOW", LongType),
      StructField("SMSYESTERDAYFLOW", LongType),
      StructField("STATSDATE", TimestampType)
    )
  )

  //表名未找到
  val STATS_SUBS_GPRSFLOW = StructType(
    Seq(StructField("MSISDN", StringType), StructField("STATDATE", DateType), StructField("OPRDATE", DateType),
      StructField("GPRSFLOW", LongType)))

  // edit by tongs 20170914 start
  val CM_CU_CUSTOMER = StructType(
    Seq(
      StructField("PROVINCEID", StringType),
      StructField("CUSTID", StringType),
      StructField("CUSTNAME", StringType),
      StructField("CUSTTYPE", LongType),
      StructField("STATUS", StringType),
      StructField("STATUSDATE", TimestampType),
      StructField("CERTTYPE", StringType),
      StructField("CERTID", StringType),
      StructField("PHONE", StringType),
      StructField("ADDR", StringType),
      StructField("EMAIL", StringType),
      StructField("POSTCODE", StringType),
      StructField("CREATEDATE", TimestampType),
      StructField("NATIONID", StringType)
    ))
  // edit by tongs 20170914 start

  val PC_PROD_COMMINFO = StructType(
    Seq(
      StructField("PRODID", StringType)
    )
  )

  val STATS_PROD_SIMFLOW_MONTH = StructType(
    Seq(StructField("MSISDN", StringType),
      StructField("SUBSID", LongType),
      StructField("ECCUSTID", StringType),
      StructField("PRODID", StringType),
      StructField("PROVINCEID", StringType),
      StructField("GPRSUSEDFLOW", LongType),
      StructField("STATUS", StringType),
      StructField("GPRSTOTAL", LongType),
      StructField("STATSDATE", DateType),
      StructField("CREATEDATE", TimestampType),
      StructField("SMSUSEDFLOW", LongType),
      StructField("SMSTOTAL", LongType)))

  // edit by tongs 20170913 start
  val SA_DB_PROVINCE = StructType(
    Seq(
      StructField("CODE", StringType),
      StructField("PLATID", StringType),
      StructField("PROVINCEID", StringType),
      StructField("PROVINCENAME", StringType),
      StructField("PROVINCESHORT", StringType)
    )
  )
  // edit by tongs 20170913 end

  // edit by tongs 20170918 start
  val STATS_PROD_SIMFLOW = StructType(
    Seq(
      StructField("MSISDN", StringType),
      StructField("SUBSID", LongType),
      StructField("ECCUSTID", StringType),
      StructField("PRODID", StringType),
      StructField("PROVINCEID", StringType),
      StructField("GPRSUSEDFLOW", LongType),
      StructField("SMSUSEDFLOW", LongType),
      StructField("GPRSYESTERDAYFLOW", LongType),
      StructField("SMSYESTERDAYFLOW", LongType),
      StructField("STATUS", StringType),
      StructField("GPRSUSEDPREC", DoubleType),
      StructField("SMSUSEDPREC", DoubleType),
      StructField("GPRSTOTAL", LongType),
      StructField("SMSTOTAL", LongType),
      StructField("STATSDATE", TimestampType),
      StructField("CREATEDATE", TimestampType),
      StructField("P_STATSDATE", StringType)
    )
  )
  // edit by tongs 20170918 end

  // edit by tongs 20170914 start
  val CM_SUBS_ECPRODUCT = StructType(
    Seq(
      StructField("ECSUBPRODUCTID", StringType),
      StructField("OPERSEQ", StringType),
      StructField("OPERCODE", StringType),
      StructField("BIZSERVICECODE", StringType),
      StructField("AUTHMODE", StringType),
      StructField("PRODUCTCODE", StringType),
      StructField("USERID", StringType),
      StructField("PRODUCTNAME", StringType),
      StructField("PRODUCTSTATUS", StringType),
      StructField("OPEREFFTIME", TimestampType),
      StructField("OPRTIME", TimestampType),
      StructField("ORDEREFDATE", TimestampType),
      StructField("STATUSDATE", TimestampType),
      StructField("ORDERSTATUS", StringType),
      StructField("EXPIREDATE", TimestampType),
      StructField("BASESERVCODE", StringType),
      StructField("BASESERVCODEPROP", StringType),
      StructField("BIZSERVMODE", StringType),
      StructField("PKGPRODID", StringType),
      StructField("RELOPRSEQ", StringType),
      StructField("INDUSTRYID", StringType),
      StructField("APPLYCHANNEL", StringType),
      StructField("CUSTID", StringType),
      StructField("PROVINCEID", StringType))
  )
  // edit by tongs 20170914 end

  // edit by tongs 20170914 start
  val CM_SUBS_ECPRODSUBSINFO = StructType(
    Seq(
      StructField("USERID", StringType),
      StructField("ISPRECHARGE", StringType),
      StructField("ISTEXTSIGN", StringType),
      StructField("DEFAULTSIGNLANG", StringType),
      StructField("TEXTSIGNEN", StringType),
      StructField("TEXTSIGNZH", StringType),
      //将StructField("ECCUSTID", StringType)中的ECCUSTID改成“CUSTID”
      StructField("CUSTID", StringType),
      StructField("SERVNUMBER", StringType),
      StructField("PRODID", StringType),
      StructField("CREATEDATE", TimestampType),
      StructField("STARTDATE", TimestampType),
      StructField("INVALIDDATE", TimestampType),
      StructField("STATUS", StringType),
      StructField("DATASOURCE", StringType),
      //增加PROVINCEID
      StructField("PROVINCEID", StringType)
    )
  )
  // edit by tongs 20170914 end

  //由CM_SUBS_SIMFLOWHIS更名为 CM_SUBS_SIMFLOW
  val CM_SUBS_SIMFLOW = StructType(
    Seq(
      StructField("MSISDN", StringType),
      StructField("SMSPRODINSTID", StringType),
      StructField("GPRSPRODINSTID", StringType),
      StructField("VOICEPRODINSTID", StringType),
      StructField("SMSUSEDFLOW", LongType),
      StructField("SMSTOTAL", LongType),
      StructField("GPRSUSEDFLOW", LongType),
      StructField("GPRSTOTAL", LongType),
      StructField("VOICEUSEDFLOW", LongType),
      StructField("VOICETOTAL", LongType),
      StructField("STARTTIME", TimestampType),
      StructField("ENDTIME", TimestampType),
      StructField("CREATEDATE", TimestampType),
      StructField("P_STARTTIME", StringType)
    )
  )

  // edit by tongs 20170914 start
  val CM_SUBS_PRODUCT = StructType(
    Seq(
      StructField("OID", LongType),
      StructField("MSISDN", StringType),
      StructField("SUBSID", LongType),
      StructField("PROVINCEID", StringType),
      StructField("PRODID", StringType),
      StructField("PRODNAME", StringType),
      StructField("STARTDATE", TimestampType),
      StructField("STATUS", StringType),
      StructField("STATUSDATE", TimestampType),
      StructField("OPERSEQ", StringType),
      StructField("OPERCODE", StringType),
      StructField("APPLYCHANNEL", StringType),
      StructField("PRODINSTID", StringType),
      StructField("PRODINSTEXPTIME", TimestampType),
      StructField("PKGPRODID", StringType),
      StructField("OPRTIME", TimestampType),
      StructField("CARDPHYSICALTYPE", StringType),
      StructField("FUTURESTATUS", StringType),
      StructField("DATASOURCE", StringType),
      StructField("ECSOURCE", StringType),
      StructField("CUSTID", StringType)
    )
  )
  // edit by tongs 20170914 end

  // edit by tongs 20170914 start
  val CM_SUBS_PRODUCT_DISUSER = StructType(
    Seq(
      StructField("OID", LongType),
      StructField("SUBSID", LongType),
      StructField("PROVINCEID", StringType),
      StructField("PRODID", StringType),
      StructField("STARTDATE", TimestampType),
      StructField("STATUS", StringType),
      StructField("STATUSDATE", TimestampType),
      StructField("OPERSEQ", StringType),
      StructField("OPERCODE", StringType),
      StructField("APPLYCHANNEL", StringType),
      StructField("PRODINSTID", StringType),
      StructField("PRODINSTEXPTIME", TimestampType),
      StructField("PKGPRODID", StringType),
      StructField("OPRTIME", TimestampType),
      StructField("CARDPHYSICALTYPE", StringType),
      StructField("FUTURESTATUS", StringType),
      StructField("DATASOURCE", StringType),
      StructField("ECSOURCE", StringType),
      StructField("B_TIME", TimestampType),
      StructField("B_REMARK", StringType)
    )
  )
  // edit by tongs 20170914 end

  //CM_SUBS_NETSTATUSHIS更名为 CM_SUBS_NETSTATUS_HIS
  val CM_SUBS_NETSTATUSHIS = StructType(
    Seq(StructField("MSISDN", StringType),
      StructField("IMSI", StringType),
      StructField("NETSTATUS", LongType),
      StructField("LOCATION", StringType),
      StructField("MAPPSI", LongType),
      StructField("CREATEDATE", TimestampType),
      StructField("ACTIONID", LongType),
      StructField("SCAN_STATUS", LongType),
      //增加PROVINCEID字段
      StructField("PROVINCEID", StringType)))

  // add by tongs 20170914 start
  val CM_SUBS_NETSTATUS_HIS = StructType(
    Seq(
      StructField("MSISDN", StringType),
      StructField("IMSI", StringType),
      StructField("PROVINCEID", StringType),
      StructField("NETSTATUS", LongType),
      StructField("LOCATION", StringType),
      StructField("MAPPSI", LongType),
      StructField("CREATEDATE", TimestampType),
      StructField("ACTIONID", LongType),
      StructField("SCAN_STATUS", LongType),
      StructField("P_CREATEDATE", StringType)
    )
  )
  // add by tongs 20170914 end

  val CM_SUBS_PRODSERVICE = StructType(
    Seq(
      StructField("SUBPRODUCTINSTID", StringType),
      StructField("SERVICEID", StringType),
      StructField("ATTRKEY", StringType),
      StructField("ATTRVALUE", StringType),
      StructField("STATUSDATE", TimestampType),
      StructField("OPRSEQ", StringType),
      StructField("SUBSID", LongType),
      StructField("PRODID", StringType),
      //增加PROVINCEID字段
      StructField("PROVINCEID", StringType)))

  //由 UDM_ACCT_TRAFFIC_DAILY_DATA 更名为  CM_SUBS_TRAFFIC_DAILY_DATA
  val CM_SUBS_ACCT_TRAFFIC_DAILY_DATA = StructType(
    //由PROVINCE_ID 变更为 PROVINCEID
    Seq(StructField("PROVINCEID", StringType),
      StructField("MSISDN", StringType),
      StructField("PROCESS_TIME", DateType),
      StructField("APN", StringType),
      //由 GPRS_TOTAL_UNITS 变更为 TOTAL_UNITS
      StructField("GPRS_TOTAL_UNITS", LongType),
      //由 GPRS_ACCT_PERIOD 变更为 ACCT_PERIOD
      StructField("GPRS_ACCT_PERIOD", StringType)))

  // edit by tongs 20170914 start
  val CM_SUBS_GPRSSTATUS = StructType(
    Seq(
      StructField("MSISDN", StringType),
      StructField("STATUS", StringType),
      StructField("STATUSDATE", TimestampType),
      StructField("APNID", StringType),
      StructField("IP", StringType),
      StructField("ACCESSTYPE", StringType),
      StructField("CREATEDATE", TimestampType),
      StructField("STATUSTIMESTAMP", LongType),
      StructField("ECCUSTID", StringType),
      StructField("P_STATUSDATE", StringType)
    )
  )
  // edit by tongs 20170914 end

  val SA_DB_PROVINCESWITCH = StructType(
    Seq(StructField("PROVINCEID", StringType),
      StructField("STATUS", StringType),
      StructField("NOTES", StringType)))

  //由 UDM_ACCT_TRAFFIC_DAILY_SMS 变更为 CM_SUBS_TRAFFIC_DAILY_SMS
  val CM_SUBS_TRAFFIC_DAILY_SMS = StructType(
    //由 PROVINCE_ID 变更为 PROVINCEID
    Seq(StructField("PROVINCEID", StringType),
      StructField("MSISDN", StringType),
      //类型由 Date 更改为 TimestampType
      StructField("PROCESS_TIME", TimestampType),
      //由 SMS_TOTAL_UNITS 更名为 TOTAL_UNITS
      StructField("TOTAL_UNITS", LongType),
      //由 SMS_ACCT_PERIOD 更名为 ACCT_PERIOD
      StructField("ACCT_PERIOD", StringType)))

  val CM_SUBS_PRODTOTAL_APN = StructType(
    Seq(StructField("SUBSID", StringType), StructField("MSISDN", StringType), StructField("TOTALGPRS", LongType),
      StructField("TOTALSMS", LongType), StructField("APN", StringType), StructField("PROVINCEID", StringType)))

  val PC_PROD_GPRSSMSDEF = StructType(
    Seq(StructField("PRODID", StringType),
      StructField("PRODNAME", StringType),
      StructField("INCLUDEGPRS", LongType),
      StructField("INCLUDEGSMS", LongType))
  )
  //去掉APN的表有，此表暂时不知道用途
  val STATS_SUBS_SIMFLOWDAY_APN = StructType(
    Seq(StructField("PROVINCEID", StringType),
      StructField("MSISDN", StringType),
      StructField("SMSTOTAL", LongType),
      StructField("SMSUSEDFLOW", LongType),
      StructField("GPRSTOTAL", LongType),
      StructField("GPRSUSEDFLOW", LongType),
      StructField("VOICETOTAL", LongType),
      StructField("VOICEUSEDFLOW", LongType),
      StructField("GPRSYESTERDAYFLOW", LongType),
      StructField("SMSYESTERDAYFLOW", LongType),
      StructField("STATSDATE", DateType),
      StructField("CUSTID", StringType),
      StructField("GROUPBELONG", StringType),
      StructField("APN", StringType),
      StructField("REMARK", StringType),
      StructField("ICCID", StringType)
    )
  )

  //未找到对应表
  val UDM_ACCT_TRAFFIC_DAILY = StructType(
    Seq(StructField("MSISDN", StringType),
      StructField("PROCESS_TIME", DateType),
      StructField("SMS_TOTAL_UNITS", LongType),
      StructField("SMS_ACCT_PERIOD", StringType),
      StructField("APN", StringType),
      StructField("GPRS_TOTAL_UNITS", LongType),
      StructField("GPRS_ACCT_PERIOD", StringType),
      StructField("VOICE_TOTAL_UNITS", LongType),
      StructField("DIALING_TOTAL_UNITS", LongType),
      StructField("CALLED_TOTAL_UNITS", LongType),
      StructField("VOICE_ACCT_PERIOD", StringType)
    ))

  val CM_SUBS_PRODUCTMAIN = StructType(
    Seq(
      //增加 MSISDN 字段
      StructField("MSISDN", StringType),
      StructField("SUBSID", LongType),
      StructField("PROVINCEID", StringType),
      StructField("PRODID", StringType),
      //增加 PRODNAME 字段
      StructField("PRODNAME", StringType),
      StructField("STARTDATE", TimestampType),
      StructField("STATUS", StringType),
      StructField("EXPTIME", TimestampType),
      StructField("PRODINSTID", StringType),
      StructField("OPRSEQ", StringType),
      StructField("OPRTYPE", StringType),
      StructField("FUTURESTATUS", StringType),
      StructField("DATASOURCE", StringType)
    )
  )

  val CM_CU_REGION = StructType(
    Seq(
      StructField("REGIONID", StringType),
      StructField("REGIONNAME", StringType),
      StructField("PROVINCEID", StringType),
      //增加 CREATEDATE
      StructField("CREATEDATE", TimestampType),
      //增加 UPDATEDATE
      StructField("UPDATEDATE", TimestampType)
    )
  )

  val SA_DB_DICTITEM = StructType(
    Seq(
      StructField("DICTID", StringType),
      StructField("DICTNAME", StringType),
      StructField("GROUPID", StringType),
      StructField("NOTE", StringType),
      //增加 LANGUAGE
      StructField("LANGUAGE", StringType),
      //增加 TYPE
      StructField("TYPE", StringType)
    )
  )

  val STATS_SEGMENT_QUERY_INFO = StructType(
    Seq(
      StructField("TABLE_NAME", StringType),
      StructField("STATDATE", DateType),
      StructField("SEGMENT_INFO", StringType),
      StructField("SEGMENT_TITLE", StringType),
      StructField("ORDER_NO", IntegerType),
      StructField("SEGMENT_COL", StringType),
      StructField("CREATEDATE", TimestampType)
    )
  )

  /*
  * 描述：以下部分为2017-09-08添加
  * */
  val EB_APP_PLAT = StructType(
    Seq(
      StructField("APPID", StringType), StructField("APPNAME", StringType), StructField("APPTYPE", StringType),
      StructField("APPPROVIDERID", StringType), StructField("APPURL", StringType), StructField("APPIP", StringType),
      StructField("APPPORT", LongType), StructField("SMS_RECEIVE_NUMBER", StringType), StructField("MMS_RECEIVE_NUMBER", StringType),
      StructField("WMMPUSERNAME", StringType), StructField("WMMPPASSWORD", StringType), StructField("VASPID", StringType),
      StructField("VASID", StringType), StructField("BASEKEY", StringType), StructField("BASEKEY_INVALIDDATE", TimestampType),
      StructField("ENCRYPTTYPE", StringType), StructField("STATUS", StringType), StructField("STATUSDATE", TimestampType),
      StructField("ONLINESTATUS", StringType), StructField("ONLINESTATUSDATE", TimestampType), StructField("TMHEARTBEAT_CIRCLE", LongType),
      StructField("CREATE_DATE", TimestampType), StructField("MAXITEMPERDAY", LongType), StructField("MAXITEMPERMON", LongType),
      StructField("SMSUSERNAME", StringType), StructField("SMSPASSWORD", StringType), StructField("MMSUSERNAME", StringType),
      StructField("MMSPASSWORD", StringType), StructField("INVALIDTIMESPAN1_START", StringType), StructField("INVALIDTIMESPAN1_END", StringType),
      StructField("INVALIDTIMESPAN2_START", StringType), StructField("INVALIDTIMESPAN2_END", StringType), StructField("INVALIDTIMESPAN3_START", StringType),
      StructField("INVALIDTIMESPAN3_END", StringType), StructField("INVALIDTIMESPAN4_START", StringType), StructField("INVALIDTIMESPAN4_END", StringType),
      StructField("SERVICETYPE", StringType), StructField("PROVINCEID", StringType), StructField("SMSCLIENTIP", StringType),
      StructField("SMSFLOW", LongType), StructField("LINKMAN", StringType), StructField("TELEPHONE", StringType),
      StructField("GWSYNCSTATUS", StringType), StructField("PBSYNCSTATUS", StringType), StructField("ACCESSTYPE", StringType),
      StructField("PRODUCTCODE", StringType), StructField("CREATEOPER", StringType), StructField("COMMLINKCHECKCYC", LongType),
      StructField("DATAPACKACKCHECKCYC", LongType), StructField("TRANSFAILUREMAXRETRANS", LongType), StructField("ADDMULTIPLELIMIT", LongType),
      StructField("ADDNUMLIMIT", LongType), StructField("DELPERCENTUM", LongType), StructField("VASPSIGNATURE", StringType),
      StructField("HTTPACCESSTYPE", StringType), StructField("HTTPCLIENTIP", StringType), StructField("HTTPCLIENTPWD", StringType),
      StructField("HTTPFLOWSECOND", StringType), StructField("MAXSMSPREDAY", LongType), StructField("APPPROVIDERTYPE", StringType), StructField("ACCOUNTNO", StringType),
      StructField("BIZ_MSG_SERVER_IP", StringType)
    )
  )

  val CM_CU_ECUSTOMER = StructType(
    Seq(
      StructField("PROVINCEID", StringType),
      StructField("GROUPID", StringType),
      StructField("FAX", StringType),
      StructField("INDUSTRYID", StringType),
      StructField("CORPTYPE", StringType),
      StructField("MGRCUSTID", StringType),
      StructField("CORPLEVEL", StringType),
      StructField("REMARK", StringType),
      StructField("OPRTIME", TimestampType),
      StructField("OPRNUM", StringType),
      StructField("ECOPRCODE", StringType),
      StructField("ECTEL", StringType),
      StructField("ECAREAID", StringType),
      StructField("JURITYPE", StringType),
      StructField("ECMANNAME", StringType),
      StructField("CMACCOUNT", StringType),
      StructField("CORPORATION", StringType),
      StructField("PARENTCUSTOMERNUMBER", StringType),
      StructField("RELOPRSEQ", StringType),
      StructField("CUSTOMERPROVINCENUMBER", StringType),
      StructField("CUSTOMERCLASSID", StringType),
      StructField("CUSTOMERRANKID", LongType),
      StructField("LOYALTYLEVELID", LongType),
      StructField("LOGINFINANCING", DoubleType),
      StructField("EMPLOYEEAMOUNTID", LongType),
      StructField("MEMBERCOUNT", LongType),
      StructField("HOMEPAGE", StringType),
      StructField("BACKGROUND", StringType),
      StructField("ORGCUSTID", StringType),
      StructField("CUSTOMERSERVLEVEL", LongType),
      StructField("CUSTID", StringType),
      StructField("CUSTNAME", StringType),
      StructField("DATASOURCE", StringType),
      StructField("LOGICREGIONID", StringType),
      StructField("LOGICREGIONTIME", TimestampType),
      StructField("APPLICATIONAREA", StringType),
      StructField("BELONGGROUP", StringType)
    )
  )

  val CM_CU_FCUSTOMER = StructType(
    Seq(
      StructField("PROVINCEID", StringType),
      StructField("FAMILYID", StringType),
      StructField("CUSTID", StringType),
      StructField("CUSTNAME", StringType),
      StructField("HOUSEHOLDERCUSTID", StringType)
    )
  )

  val CM_CU_GROUPINFO = StructType(
    Seq(
      StructField("GROUPID", StringType),
      StructField("GROUPNAME", StringType),
      StructField("CREATOR", StringType),
      StructField("GROUPBELONG", StringType),
      StructField("NOTE", StringType),
      StructField("CONTACTS", StringType),
      StructField("PHONE", StringType),
      StructField("CREATEDATE", TimestampType),
      StructField("UPDATEDATE", TimestampType)
    )
  )

  val CM_CU_PCUSTOMER = StructType(
    Seq(StructField("PROVINCEID", StringType),
      StructField("GENDER", IntegerType),
      StructField("BIRTHDATE", TimestampType),
      StructField("NATION", StringType),
      StructField("RELIGION", StringType),
      StructField("POLITICAL", StringType),
      StructField("MARRIAGE", StringType),
      StructField("CHILDREN", StringType),
      StructField("EDUCATION", StringType),
      StructField("SALARY", StringType),
      StructField("INDUSTRY", StringType),
      StructField("COMPANY", StringType),
      StructField("DUTY", StringType),
      StructField("FAVERATE", StringType),
      StructField("ADDR", StringType),
      StructField("CUSTID", StringType),
      StructField("CUSTNAME", StringType)
    )
  )

  val CM_SUBS_APPBIND = StructType(
    Seq(
      StructField("MSISDN", StringType),
      StructField("APPID", StringType),
      StructField("BINDDATE", TimestampType),
      StructField("ECID", StringType),
      StructField("PROVINCEID", StringType)
    )
  )

  val CM_SUBS_BILLING_MONTHLY = StructType(
    Seq(
      StructField("CUST_TYPE", StringType),
      StructField("CUST_NO", StringType),
      StructField("MSISDN", StringType),
      StructField("PROVINCEID", StringType),
      StructField("PROCESS_TIME", TimestampType),
      StructField("TOTAL_REVENUE", DoubleType),
      StructField("PERSONAL_BALANCE", DoubleType),
      StructField("TYPE", StringType),
      StructField("NAME", StringType),
      StructField("VALUE", DoubleType)
    )
  )

  // edit by tongs 20170914 start
  val CM_SUBS_GPRSFLOW = StructType(
    Seq(
      StructField("MSISDN", StringType),
      StructField("STATDATE", TimestampType),
      StructField("OPRDATE", TimestampType),
      StructField("GPRSFLOW", LongType),
      StructField("P_STATDATE", StringType)
    )
  )
  // edit by tongs 20170914 end

  val CM_SUBS_PRODSERVICE_HIS = StructType(
    Seq(
      StructField("SUBPRODUCTINSTID", StringType),
      StructField("SERVICEID", StringType),
      StructField("ATTRKEY", StringType),
      StructField("ATTRVALUE", StringType),
      StructField("STATUSDATE", TimestampType),
      StructField("OPRSEQ", StringType),
      StructField("SUBSID", LongType),
      StructField("PRODID", StringType),
      StructField("PROVINCEID", StringType)
    )
  )

  val CM_SUBS_PRODUCTHIS_DISUSER = StructType(
    Seq(
      StructField("OID", LongType),
      StructField("SUBSID", LongType),
      StructField("PROVINCEID", StringType),
      StructField("PRODID", StringType),
      StructField("STARTDATE", TimestampType),
      StructField("STATUS", StringType),
      StructField("STATUSDATE", TimestampType),
      StructField("OPERSEQ", StringType),
      StructField("OPERCODE", StringType),
      StructField("APPLYCHANNEL", StringType),
      StructField("PRODINSTID", StringType),
      StructField("PRODINSTEXPTIME", TimestampType),
      StructField("PKGPRODID", StringType),
      StructField("PRESEQ", StringType),
      StructField("OPRTIME", TimestampType),
      StructField("B_TIME", TimestampType),
      StructField("B_REMARK", StringType)
    )
  )

  val CM_SUBS_PRODUCTMAINHIS_DISUSER = StructType(
    Seq(
      StructField("SUBSID", LongType),
      StructField("PROVINCEID", StringType),
      StructField("PRODID", StringType),
      StructField("STARTDATE", TimestampType),
      StructField("STATUS", StringType),
      StructField("EXPTIME", TimestampType),
      StructField("PRODINSTID", StringType),
      StructField("OPRSEQ", StringType),
      StructField("OPRTYPE", StringType),
      StructField("B_TIME", TimestampType),
      StructField("B_REMARK", StringType)
    )
  )

  val CM_SUBS_PRODUCTMAIN_DISUSER = StructType(
    Seq(
      StructField("SUBSID", LongType),
      StructField("PROVINCEID", StringType),
      StructField("PRODID", StringType),
      StructField("STARTDATE", TimestampType),
      StructField("STATUS", StringType),
      StructField("EXPTIME", TimestampType),
      StructField("PRODINSTID", StringType),
      StructField("OPRSEQ", StringType),
      StructField("OPRTYPE", StringType),
      StructField("FUTURESTATUS", StringType),
      StructField("DATASOURCE", StringType),
      StructField("B_TIME", TimestampType),
      StructField("B_REMARK", StringType)
    )
  )

  val CM_SUBS_PRODUCTMAIN_HIS = StructType(
    Seq(
      StructField("MSISDN", StringType),
      StructField("SUBSID", LongType),
      StructField("PROVINCEID", StringType),
      StructField("PRODID", StringType),
      StructField("PRODNAME", StringType),
      StructField("STARTDATE", TimestampType),
      StructField("STATUS", StringType),
      StructField("EXPTIME", TimestampType),
      StructField("PRODINSTID", StringType),
      StructField("OPRSEQ", StringType),
      StructField("OPRTYPE", StringType)
    )
  )

  val CM_SUBS_PRODUCT_HIS = StructType(
    Seq(
      StructField("OID", IntegerType),
      StructField("MSISDN", StringType),
      StructField("SUBSID", LongType),
      StructField("PROVINCEID", StringType),
      StructField("PRODID", StringType),
      StructField("PRODNAME", StringType),
      StructField("STARTDATE", TimestampType),
      StructField("STATUS", StringType),
      StructField("STATUSDATE", TimestampType),
      StructField("OPERSEQ", StringType),
      StructField("OPERCODE", StringType),
      StructField("APPLYCHANNEL", StringType),
      StructField("PRODINSTID", StringType),
      StructField("PRODINSTEXPTIME", TimestampType),
      StructField("PKGPRODID", StringType),
      StructField("OPRTIME", TimestampType),
      StructField("CARDPHYSICALTYPE", StringType),
      StructField("PRESEQ", StringType),
      StructField("CUSTID", StringType)
    )
  )

  val CM_SUBS_SIMFLOW_DAY = StructType(
    Seq(
      StructField("MSISDN", StringType),
      StructField("SMSPRODINSTID", StringType),
      StructField("GPRSPRODINSTID", StringType),
      StructField("VOICEPRODINSTID", StringType),
      StructField("SMSUSEDFLOW", LongType),
      StructField("SMSTOTAL", DoubleType),
      StructField("GPRSUSEDFLOW", DoubleType),
      StructField("GPRSTOTAL", DoubleType),
      StructField("VOICEUSEDFLOW", DoubleType),
      StructField("VOICETOTAL", DoubleType),
      StructField("STATSDATE", TimestampType),
      StructField("CREATEDATE", TimestampType),
      StructField("GPRSYESTERDAYFLOW", DoubleType),
      StructField("SMSYESTERDAYFLOW", DoubleType)
    )
  )

  val CM_SUBS_SMS = StructType(
    Seq(
      StructField("SMSSEQ", StringType),
      StructField("SMSTYPE", StringType),
      StructField("USERTYPE", StringType),
      StructField("MSISDN", StringType),
      StructField("SUBSID", LongType),
      StructField("CUSTID", StringType),
      StructField("PROVINCEID", StringType),
      StructField("THIRDPARTNUMBER", StringType),
      StructField("SERVCODE", StringType),
      StructField("BUSINESSCODE", StringType),
      StructField("CHARGINGTYPE", StringType),
      StructField("FUNCTIONCOST", StringType),
      StructField("MONTHCOST", StringType),
      StructField("SMSSENDSTATUS", StringType),
      StructField("SMSSENDPRIORITY", StringType),
      StructField("SMSLENGTH", StringType),
      StructField("MSISDNBELONG", StringType),
      StructField("GATEWAYCODE", StringType),
      StructField("FORWARDGATEWAYCODE", StringType),
      StructField("SMSCCODE", StringType),
      StructField("APPLYTIME", TimestampType),
      StructField("DEALENDTIME", TimestampType)
    )
  )

  // edit by tongs 20170914 start
  val CM_SUBS_SMSFLOW = StructType(
    Seq(
      StructField("MSISDN", StringType),
      StructField("STATDATE", TimestampType),
      StructField("OPRDATE", TimestampType),
      StructField("SMSFLOW_STANDARD", LongType),
      StructField("SMSFLOW_CUSTOMIZE", LongType),
      StructField("P_STATDATE", StringType)
    )
  )
  // edit by tongs 20170914 end

  val EB_INFO_APPLY = StructType(
    Seq(
      StructField("APIID", StringType),
      StructField("APIEBID", StringType),
      StructField("APIEPID", StringType),
      StructField("APICODE", StringType),
      StructField("APINAME", StringType),
      StructField("APIDES", StringType),
      StructField("APIURL", StringType),
      StructField("APIMSG", StringType),
      StructField("APINOTE", StringType),
      StructField("APIBELONGEC", StringType),
      StructField("APIBELONGNET", StringType),
      StructField("APITYPE", StringType),
      StructField("APISP", StringType),
      StructField("APISTATUS", StringType),
      StructField("CREATEDATE", TimestampType),
      StructField("APIOPERID", StringType),
      StructField("OPERNAME", StringType),
      StructField("APPROVNAME", StringType),
      StructField("APPROVID", StringType),
      StructField("APPROVDATE", TimestampType),
      StructField("APPROVOPINION", StringType),
      StructField("ENABLESTATUS", StringType),
      StructField("FLOWSECOND", StringType),
      StructField("FLOWFLAG", StringType),
      StructField("APIBELONGTYPE", StringType),
      StructField("VIPPOOL", StringType)
    )
  )

  val EB_INFO_PROVIDER = StructType(
    Seq(
      StructField("EPID", StringType),
      StructField("EPNAME", StringType),
      StructField("SERVICETEL", StringType),
      StructField("SERVICEDESC", StringType),
      StructField("CONTACTMAN", StringType),
      StructField("CONTACTPHONE", StringType),
      StructField("PRINCIPAL", StringType),
      StructField("PCERTYPE", StringType),
      StructField("PCERTID", StringType),
      StructField("CREATEDATE", TimestampType),
      StructField("HELPINFO", StringType),
      StructField("REWARDPLAN", StringType),
      StructField("OPERID", StringType),
      StructField("STATUS", StringType),
      StructField("SYNCSTATUS", StringType),
      StructField("OPRCODE", StringType),
      StructField("OPERTIME", TimestampType)
    )
  )

  val EB_SUBS_ENABLER = StructType(
    Seq(
      StructField("OID", LongType),
      StructField("APPLYOID", StringType),
      StructField("APPID", StringType),
      StructField("EBID", StringType),
      StructField("ISUSEWHITELIST", StringType),
      StructField("TARIFFID", StringType),
      StructField("TESTSTARTDATE", TimestampType),
      StructField("TESTENDDATE", TimestampType),
      StructField("STARTDATE", TimestampType),
      StructField("ENDDATE", TimestampType),
      StructField("STATUS", StringType),
      StructField("AUDITDATE", TimestampType),
      StructField("TARIFFDISCOUNT", DoubleType),
      StructField("PERIODCOUNT", LongType),
      StructField("USAGEAMOUNT", LongType),
      StructField("PERIODAMOUNT", LongType),
      StructField("SYNCSTATUS", StringType)
    )
  )

  val SA_SR_OPER = StructType(
    Seq(
      StructField("ADDRESS", StringType),
      StructField("AUTHORITY", StringType),
      StructField("CREATEDATE", TimestampType),
      StructField("CREATEOPERCOUNT", IntegerType),
      StructField("CREATOR", StringType),
      StructField("EMAIL", StringType),
      StructField("ENDDATE", TimestampType),
      StructField("FAX", StringType),
      StructField("GENDER", IntegerType),
      StructField("LASTLOGINDATE", TimestampType),
      StructField("NOTE", StringType),
      StructField("OFFICETEL", StringType),
      StructField("OPERBELONG", StringType),
      StructField("OPERID", StringType),
      StructField("OPERNAME", StringType),
      StructField("PASSWORD", StringType),
      StructField("PBOSSSYNCSTATUS", StringType),
      StructField("PHONE", StringType),
      StructField("PROVINCEID", StringType),
      StructField("PWDHIS1", StringType),
      StructField("PWDHIS2", StringType),
      StructField("PWDHIS3", StringType),
      StructField("PWDINITDATE", TimestampType),
      StructField("PWDMODIFYDATE", TimestampType),
      StructField("ROLETYPEID", StringType),
      StructField("STATUS", StringType),
      StructField("STATUSDATE", TimestampType)
    )
  )

  val SA_SR_ROLEAUTH = StructType(
    Seq(
      StructField("AUTHID", StringType),
      StructField("ROLEID", StringType)
    )
  )

  val SA_SR_ROLEAUTH_CMP = StructType(
    Seq(
      StructField("ROLEID", StringType),
      StructField("AUTHID", StringType)
    )
  )

  val SA_DB_SYSPARAM = StructType(
    Seq(
      StructField("PARAMID", StringType),
      StructField("PARAMNAME", StringType),
      StructField("PARAMDESC", StringType),
      StructField("PARAMVALUE", StringType),
      StructField("ALLOWMODIFY", StringType),
      StructField("CREATEDATE", TimestampType),
      StructField("NOTE", StringType),
      StructField("PARAMBELONG", StringType)
    )
  )

  val CM_SUBS_GPRSSTATUS_HIS = StructType(
    Seq(
      StructField("MSISDN", StringType),
      StructField("PROVICEID", StringType),
      StructField("STATUS", StringType),
      StructField("STATUSDATE", StringType),
      StructField("APNID", StringType),
      StructField("IP", StringType),
      StructField("ACCESSTYPE", StringType),
      StructField("CREATEDATE", StringType),
      StructField("STATUSTIMESTAMP", StringType),
      StructField("ACCTINPUTOCTETS", LongType),
      StructField("ACCTOUTPUTOCTETS", LongType),
      StructField("IMSI", StringType),
      StructField("SGSN", StringType),
      StructField("GGSN", StringType),
      StructField("IMEI", StringType),
      StructField("USERLOCATIONINFO", StringType),
      StructField("TERMINATECAUSE", StringType)
    )
  )

  val STATS_GLOBAL_SUBS_4G_DAY = StructType(
    Seq(
      StructField("PROVINCEID", StringType),
      StructField("PROVINCENAME", StringType),
      StructField("TOTALCNT", LongType),
      StructField("TOTAL4GCNT", LongType),
      StructField("TOTAL4GPERC", DoubleType),
      StructField("EC4GPERC", DoubleType),
      StructField("PERSON4GPERC", DoubleType),
      StructField("INCREASE4GCNT", LongType),
      StructField("ADD4GCNT", LongType),
      StructField("STATSDATE", TimestampType),
      StructField("CREATEDATE", TimestampType),
      StructField("EC4GCNT", LongType),
      StructField("PERSON4GCNT", LongType),
      StructField("P_STATSDATE", StringType)

    )
  )

  val CM_SUBS_PROD_REL_DAY_1 = StructType(
    Seq(
      StructField("PROVINCEID", StringType),
      StructField("CUSTID", StringType),
      StructField("CUSTTYPE", IntegerType),
      StructField("SUBSID", StringType),
      StructField("MSISDN", StringType),
      StructField("STATUS", StringType),
      StructField("STARTDATE", TimestampType),
      StructField("STATUSDATE", TimestampType),
      StructField("PRODID", StringType),
      StructField("PROD_STATUS", StringType)
    )
  )

  val STATS_PRODSUBSCRIBER_DAY = StructType(
    Seq(
      StructField("PRODID", StringType),
      StructField("PRODNAME", StringType),
      StructField("TOTALCNT", LongType),
      StructField("ADDCNT", LongType),
      StructField("DELCNT", LongType),
      StructField("INCREASECNT", LongType),
      StructField("STATSDATE", TimestampType),
      StructField("CREATEDATE", TimestampType),
      StructField("PROVINCEID", StringType),
      StructField("PROVINCENAME", StringType),
      StructField("NUM_KEY", StringType),
      StructField("NUM_CNT", LongType),
      StructField("P_STATSDATE", StringType)
    )
  )

  val STATS_EC_SUBS_DAY = StructType(
    Seq(
      StructField("PROVINCEID", StringType),
      StructField("PROVINCENAME", StringType),
      StructField("CUSTID", StringType),
      StructField("CUSTNAME", StringType),
      StructField("TOTALCNT", LongType),
      StructField("ADDCNT", LongType),
      StructField("INCREASECNT", LongType),
      StructField("LINKEDINDEX", DoubleType),
      StructField("DELCNT", LongType),
      StructField("DELPERC", DoubleType),
      StructField("ACTIVECNT", LongType),
      StructField("ACTIVEPERC", DoubleType),
      StructField("GPRSACTIVECNT", LongType),
      StructField("GPRSACTIVEPERC", DoubleType),
      StructField("SMSACTIVECNT", LongType),
      StructField("SMSACTIVEPERC", DoubleType),
      StructField("STATSDATE", TimestampType),
      StructField("CREATEDATE", TimestampType),
      StructField("P_STATSDATE", StringType)
    )
  )

  val STATS_EC_SUBS_MONTH = StructType(
    Seq(
      StructField("PROVINCEID", StringType),
      StructField("PROVINCENAME", StringType),
      StructField("CUSTID", StringType),
      StructField("CUSTNAME", StringType),
      StructField("TOTALCNT", LongType),
      StructField("ADDCNT", LongType),
      StructField("INCREASECNT", LongType),
      StructField("LINKEDINDEX", DoubleType),
      StructField("DELCNT", LongType),
      StructField("DELPERC", DoubleType),
      StructField("ACTIVECNT", LongType),
      StructField("ACTIVEPERC", DoubleType),
      StructField("GPRSACTIVECNT", LongType),
      StructField("GPRSACTIVEPERC", DoubleType),
      StructField("SMSACTIVECNT", LongType),
      StructField("SMSACTIVEPERC", DoubleType),
      StructField("STATPERIOD", StringType),
      StructField("STATSDATE", TimestampType),
      StructField("CREATEDATE", TimestampType),
      StructField("P_STATSDATE", StringType)
    )
  )

  // add by tongs 20170911 start
  val STATS_CUST_TMSUBS = StructType(
    Seq(
      StructField("CUSTNAME", StringType),
      StructField("SUBSCOUNT", LongType),
      StructField("UNSUBSCOUNT", LongType),
      StructField("STATSDATE", TimestampType),
      StructField("PROVINCEID", StringType),
      StructField("STATPERIOD", StringType),
      StructField("CREATEDATE", TimestampType),
      StructField("CUSTID", StringType),
      StructField("P_STATSDATE", StringType)
    )
  )
  // add by tongs 20170911 end

  // add by tongs 20170912 start
  val STATS_SUBS_SIMFLOW_M_TMP = StructType(
    Seq(
      StructField("PROVINCEID", StringType),
      StructField("MSISDN", StringType),
      StructField("GPRSTOTAL", LongType),
      StructField("GPRSUSEDFLOW", LongType),
      StructField("STATSDATE", TimestampType),
      StructField("P_STATSDATE", StringType)
    )
  )
  // add by tongs 20170912 end

  // add by zeng start
  val STATS_EC_SUBS_DAY_4G = StructType(
    Seq(
      StructField("PROVINCEID", StringType),
      StructField("PROVINCENAME", StringType),
      StructField("CUSTID", StringType),
      StructField("CUSTNAME", StringType),
      StructField("TOTALCNT", LongType),
      StructField("TOTALGCNT", LongType),
      StructField("ADDCNT", LongType),
      StructField("INCREASECNT", LongType),
      StructField("GPERCENT", DoubleType),
      StructField("STATSDATE", TimestampType),
      StructField("CREATEDATE", TimestampType),
      StructField("P_STATSDATE", StringType)
    )
  )
  val STATS_PRODSUBSCRIBER_WEEK = StructType(
    Seq(
      StructField("PRODID", StringType),
      StructField("PRODNAME", StringType),
      StructField("TOTALCNT", LongType),
      StructField("ADDCNT", LongType),
      StructField("DELCNT", LongType),
      StructField("INCREASECNT", LongType),
      StructField("STATSDATE", TimestampType),
      StructField("CREATEDATE", TimestampType),
      StructField("PROVINCEID", StringType),
      StructField("STATPERIOD", StringType),
      StructField("PROVINCENAME", StringType),
      StructField("NUM_KEY", StringType),
      StructField("NUM_CNT", LongType),
      StructField("P_STATSDATE", StringType)
    )
  )

  val STATS_PRODSUBSCRIBER_MONTH = StructType(
    Seq(
      StructField("PRODID", StringType),
      StructField("PRODNAME", StringType),
      StructField("TOTALCNT", LongType),
      StructField("ADDCNT", LongType),
      StructField("DELCNT", LongType),
      StructField("INCREASECNT", LongType),
      StructField("STATSDATE", TimestampType),
      StructField("CREATEDATE", TimestampType),
      StructField("PROVINCEID", StringType),
      StructField("STATPERIOD", StringType),
      StructField("PROVINCENAME", StringType),
      StructField("NUM_KEY", StringType),
      StructField("NUM_CNT", LongType),
      StructField("P_STATSDATE", StringType)
    )
  )

  // 添加P_STATDATE字段
  val GPRSFLOW = StructType(
    Seq(
      StructField("MSISDN", StringType),
      StructField("STATDATE", TimestampType),
      StructField("OPRDATE", TimestampType),
      StructField("GPRSFLOW", LongType),
      // zeng add
      StructField("P_STATDATE", StringType)
    )
  )

  val SMSFLOW = StructType(
    Seq(
      StructField("MSISDN", StringType),
      StructField("STATDATE", TimestampType),
      StructField("OPRDATE", TimestampType),
      StructField("SMSFLOW_STANDARD", LongType),
      StructField("SMSFLOW_CUSTOMIZE", LongType),
      StructField("P_STATDATE", StringType)
    )
  )
  //add by zeng end

  val TEST = StructType(
    Seq(
      StructField("F0", StringType),
      StructField("F1", LongType)
    )
  )
}