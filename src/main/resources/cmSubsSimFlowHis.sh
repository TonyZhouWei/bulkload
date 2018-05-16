appVersion=3.2.2
numExecutors="20"
archiveFile=""
clearFile="P2OMS"
sourceTable="CM_SUBS_SIMFLOWHIS"
deployMode="cluster"
#deployMode="client"
sparkVersion="2"
master="yarn"
queue="bigdata"
releasePath="/home/stats/release"


statsDate=`date +%Y%m%d`
dataDate=`date -d "-7 day" +%Y%m%d`
lastMonthDate=`date -d last-month +%Y%m%d`
nextMonthDate=`date -d next-month +%Y%m%d`
statsJar=`ls $releasePath/bigdata-stat-${appVersion}-jar-with-dependencies.jar`
statsAJar=`ls $releasePath/bigdata-stat-${appVersion}-jar-with-dependenciesA.jar`
set -x

numExecutors="1"
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --jars ${releasePath}/spark-xml_2.11-0.4.1.jar --num-executors $numExecutors --name FORMAT_CM_SUBS_SIMFLOWHIS --class com.cmiot.FormatFile $statsJar $sourceTable
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutors --name CLEAR_CM_SUBS_SIMFLOWHIS --class com.cmiot.ClearData $statsJar $nextMonthDate $clearFile
numExecutors="6"
if [ `date +%w` -eq 1 ]; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutors --name FORMAT_WEEK_SOURCE_TABLE --class com.cmiot.FormatFile $statsJar CM_SUBS_PRODUCTMAIN,CM_SUBS_PRODUCT_DISUSER; fi
if [ `date -d $statsDate +%d` -eq "01" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutors --name FORMAT_MONTH_SOURCE_TABLE --class com.cmiot.FormatFile $statsJar CM_SUBS_NETSTATUSHIS,CM_CU_REGION,CM_SUBS_PRODUCT_DISUSER; fi
numExecutors="20"
if [ `date -d $statsDate +%d` -eq "01" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutors --name StatsMonthApp --class com.cmiot.app.StatsMonthApp $statsJar $statsDate; fi
if [ `date +%w` -eq 1 ]; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutors --name StatsWeekApp --class com.cmiot.app.StatsWeekApp $statsJar $statsDate; fi
#numExecutorsWrite="10"
#if [ `date -d $statsDate +%d` -eq "01" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_TM_FLOW_MONTH_HBASE --class com.cmiot.WriteToHBase $statsJar -d $dataDate -f STATS_EC_TM_FLOW_MONTH; fi &
numExecutorsWrite="3"
if [ `date +%w` -eq 1 ]; then  time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_PRODGPRS_WEEK --class com.cmiot.WriteToDB $statsJar $dataDate STATS_PRODGPRS_WEEK; fi
if [ `date +%w` -eq 1 ]; then  time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_PROVSUBSCRIBER_WEEK --class com.cmiot.WriteToDB $statsJar $dataDate STATS_PROVSUBSCRIBER_WEEK; fi
if [ `date +%w` -eq 1 ]; then  time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_PROVSUBSCRIBER_WEEK_TITLE --class com.cmiot.WriteNumberInfo $statsJar $dataDate STATS_PROVSUBSCRIBER_WEEK; fi
if [ `date +%w` -eq 1 ]; then  time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_PRODGPRS_WEEK --class com.cmiot.WriteToDB $statsJar $dataDate STATS_EC_PRODGPRS_WEEK; fi
if [ `date -d $statsDate +%d` -eq "01" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_FLOW_MONTH --class com.cmiot.WriteToDBApn $statsJar $lastMonthDate STATS_EC_FLOW_MONTH; fi
if [ `date -d $statsDate +%d` -eq "01" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_GROUP_FLOW_MONTH --class com.cmiot.WriteToDBApn $statsJar $lastMonthDate STATS_EC_GROUP_FLOW_MONTH; fi
if [ `date -d $statsDate +%d` -eq "01" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_TM_FLOW_MONTH --class com.cmiot.WriteToDBApn $statsJar $lastMonthDate STATS_EC_TM_FLOW_MONTH; fi
if [ `date -d $statsDate +%d` -eq "01" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_SUBS_SIMFLOWMONTH --class com.cmiot.WriteToDB $statsAJar $lastMonthDate STATS_SUBS_SIMFLOWMONTH ; fi
if [ `date -d $statsDate +%d` -eq "01" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_FLOW_POOL_USER_MONTH --class com.cmiot.WriteToDB $statsAJar $lastMonthDate STATS_EC_FLOW_POOL_USER_MONTH ; fi
if [ `date -d $statsDate +%d` -eq "01" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_SUBS_ACTIVE_REGION_MONTH --class com.cmiot.WriteToDB $statsJar $lastMonthDate STATS_SUBS_ACTIVE_REGION_MONTH ; fi
if [ `date -d $statsDate +%d` -eq "01" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_SUBS_ACTIVE_REGION_MONTH_TITLE --class com.cmiot.WriteNumberInfo $statsJar $lastMonthDate STATS_SUBS_ACTIVE_REGION_MONTH ; fi
if [ `date -d $statsDate +%d` -eq "01" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_SUBS_ONLINE_MONTH --class com.cmiot.WriteToDB $statsJar $lastMonthDate STATS_SUBS_ONLINE_MONTH ; fi
if [ `date -d $statsDate +%d` -eq "01" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_SUBS_ONLINE_MONTH_TITLE --class com.cmiot.WriteNumberInfo $statsJar $lastMonthDate STATS_SUBS_ONLINE_MONTH ; fi
#load data to alluxio
if [ `date -d $statsDate +%d` -eq "01" ] ; then /alluxio/alluxio-1.4.0/bin/alluxio fs load /stats/data/prod/STATS_EC_FLOW_MONTH.parquet; fi
if [ `date -d $statsDate +%d` -eq "01" ] ; then /alluxio/alluxio-1.4.0/bin/alluxio fs load /stats/data/prod/STATS_EC_GROUP_FLOW_MONTH.parquet; fi