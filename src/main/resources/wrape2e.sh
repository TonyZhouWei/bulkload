export ORACLE_BASE=/home/oracle
export ORACLE_HOME=$ORACLE_BASE/11gr2/
export PATH=$PATH:$ORACLE_HOME/bin
export ORACLE_SID=iotdb
export NLS_LANG='SIMPLIFIED CHINESE_CHINA.AL32UTF8'
sqlplus iot_app_read/M2mcq49@iotb << !
truncate table STATS_EC_GROUP_SUBS_FLOW;
truncate table STATS_EC_SUBS_FLOW;
ALTER index IDX_ECSUBSFLOW_CUSTID rebuild;
exit;
!
if [ $? -ne 0 ]; then exit; fi

numExecutors="20"
archiveFile="SMS_STAT,GPRS_STAT,Accounting"
sourceTable="CM_SUBS_SUBSCRIBER,CM_SUBS_SUBSCRIBER_DISUSER,SA_DB_PROVINCE,PC_PROD_COMMINFO,PC_PROD_GPRSSMSDEF,CM_SUBS_PRODUCT,CM_SUBS_PRODUCT_DISUSER,CM_SUBS_ECPRODSUBSINFO,CM_SUBS_ECPRODUCT,SA_DB_PROVINCESWITCH,CM_CU_CUSTOMER,CM_CU_ECUSTOMER,APP_INFO_PLAT,CM_SUBS_NETSTATUSHIS,CM_SUBS_PRODUCTMAIN,CM_SUBS_PRODSERVICE"
deployMode="cluster"
#deployMode="client"
sparkVersion="2"
master="yarn"
releasePath="/home/stats/release"

#statsDate=`date -d last-day +%Y%m%d`
#statsDate=`+%Y%m%d`
#dataDate=`date -d last-day +%Y%m%d`
#lastMonthDate=`date -d last-month +%Y%m%d`
#statsDate=20170115 (指定日期重跑）
#statsDate=`date -d last-month +%Y%m%d`
#dataDate=`echo $statsDate |xargs -I{} date -d "1 day ago {} " +%Y%m%d`
#lastMonthDate=`echo $statsDate |xargs -I{} date -d "last-month {} " +%Y%m%d`

statsDate=`date +%Y%m%d`
dataDate=`date -d last-day +%Y%m%d`
lastMonthDate=`date -d last-month +%Y%m%d`

statsJar=`ls $releasePath/bigdata-stat-3.2.0-jar-with-dependencies.jar`

set -x

numExecutors="3"
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue stats --master $master --deploy-mode $deployMode --num-executors $numExecutors --class com.cmiot.FormatFile $statsJar $sourceTable

numExecutors="20"
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue stats --master $master --deploy-mode $deployMode --num-executors $numExecutors --class com.cmiot.StatsApp $statsJar $statsDate
if [ `date -d $statsDate +%d` -eq "02" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --master $master --deploy-mode $deployMode --num-executors $numExecutors --class com.cmiot.ClearData $statsJar $archiveFile; fi


numExecutorsWrite=1
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue stats --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_SUBS_FLOW --class com.cmiot.WriteToDB $statsJar $dataDate STATS_EC_SUBS_FLOW.parquet STATS_EC_SUBS_FLOW &
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue stats --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_SUBS_FLOW_DAY --class com.cmiot.WriteToDB $statsJar $dataDate STATS_SUBS_FLOW_DAY.parquet STATS_SUBS_FLOW_DAY &
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue stats --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_FLOW_DAY --class com.cmiot.WriteToDB $statsJar $dataDate STATS_EC_FLOW_DAY.parquet STATS_EC_FLOW_DAY
if [ `date -d $statsDate +%d` -eq "01" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue stats --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_FLOW_MONTH --class com.cmiot.WriteToDB $statsJar $lastMonthDate STATS_EC_FLOW_MONTH.parquet STATS_EC_FLOW_MONTH; fi
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue stats --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_GROUP_FLOW_DAY --class com.cmiot.WriteToDB $statsJar $dataDate STATS_EC_GROUP_FLOW_DAY.parquet STATS_EC_GROUP_FLOW_DAY
if [ `date -d $statsDate +%d` -eq "01" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue stats --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_GROUP_FLOW_MONTH --class com.cmiot.WriteToDB $statsJar $lastMonthDate STATS_EC_GROUP_FLOW_MONTH.parquet STATS_EC_GROUP_FLOW_MONTH; fi
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue stats --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_GROUP_SUBS_FLOW --class com.cmiot.WriteToDB $statsJar $dataDate STATS_EC_GROUP_SUBS_FLOW.parquet STATS_EC_GROUP_SUBS_FLOW
if [ `date -d $statsDate +%d` -eq "01" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue stats --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_TM_FLOW_MONTH --class com.cmiot.WriteToDB $statsJar $lastMonthDate STATS_EC_TM_FLOW_MONTH.parquet STATS_EC_TM_FLOW_MONTH; fi
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue stats --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_GROUP_SUBS_FLOW --class com.cmiot.WriteToDB $statsJar $dataDate STATS_PROVSUBSCRIBER_DAY.parquet STATS_PROVSUBSCRIBER_DAY
#time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_SUBS_SIMFLOWDAY_SPK --class com.cmiot.WriteToDB $statsJar $dataDate STATS_SUBS_SIMFLOWDAY.parquet STATS_SUBS_SIMFLOWDAY_SPK