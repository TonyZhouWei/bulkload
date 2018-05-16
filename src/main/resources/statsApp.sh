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

deployMode="cluster"
sparkVersion="2"
master="yarn"
releasePath="/home/stats/release"
appVersion=3.2.2
queue="bigdata"

statsDate=`date +%Y%m%d`
dataDate=`date -d last-day +%Y%m%d`
lastMonthDate=`date -d last-month +%Y%m%d`

statsJar=`ls $releasePath/bigdata-stat-${appVersion}-jar-with-dependencies.jar`
statsAJar=`ls $releasePath/bigdata-stat-${appVersion}-jar-with-dependenciesA.jar`

set -x

numExecutors="20"
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutors --name StatsDayApp --class com.cmiot.app.StatsDayApp $statsJar $statsDate
if [ `date -d $statsDate +%d` -eq "02" ] ; then time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutors --class com.cmiot.ClearData $statsJar $archiveFile; fi

{
numExecutorsWrite="3"
 time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_GROUP_SUBS_FLOW_HBASE --class com.cmiot.WriteToHBase $statsJar -f STATS_EC_GROUP_SUBS_FLOW -d $dataDate
 time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_SUBS_FLOW_HBASE --class com.cmiot.WriteToHBase $statsJar -f STATS_EC_SUBS_FLOW -d $dataDate
 #time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_SUBS_FLOW_DAY_HBASE --class com.cmiot.WriteToHBase $statsJar -d $dataDate -f STATS_SUBS_FLOW_DAY
}&

{
numExecutorsWrite="3"
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_SUBS_FLOW --class com.cmiot.WriteToDBApn $statsJar $dataDate STATS_EC_SUBS_FLOW&
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_SUBS_FLOW_DAY --class com.cmiot.WriteToDBApn $statsJar $dataDate STATS_SUBS_FLOW_DAY&
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_FLOW_DAY --class com.cmiot.WriteToDBApn $statsJar $dataDate STATS_EC_FLOW_DAY
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_GROUP_FLOW_DAY --class com.cmiot.WriteToDBApn $statsJar $dataDate STATS_EC_GROUP_FLOW_DAY
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_GROUP_SUBS_FLOW --class com.cmiot.WriteToDBApn $statsJar $dataDate STATS_EC_GROUP_SUBS_FLOW
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_PROVSUBSCRIBER_DAY --class com.cmiot.WriteToDB $statsJar $dataDate STATS_PROVSUBSCRIBER_DAY
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_PROVSUBSCRIBER_DAY_TITLE --class com.cmiot.WriteNumberInfo $statsJar $dataDate STATS_PROVSUBSCRIBER_DAY
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name A_STAT_SUBS_SIMFLOW --class com.cmiot.WriteToDB $statsAJar $dataDate STAT_SUBS_SIMFLOW
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_GPRS_DAY --class com.cmiot.WriteToDB $statsJar $dataDate STATS_EC_GPRS_DAY
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_EC_SUBS_DAY --class com.cmiot.WriteToDB $statsJar $dataDate STATS_EC_SUBS_DAY
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_PERSON_SUBS_DAY --class com.cmiot.WriteToDB $statsJar $dataDate STATS_PERSON_SUBS_DAY
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_SUBSCRIBER_DAY --class com.cmiot.WriteToDB $statsJar $dataDate STATS_SUBSCRIBER_DAY
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_SUBSCRIBER_DAY_TITLE --class com.cmiot.WriteNumberInfo $statsJar $dataDate STATS_SUBSCRIBER_DAY
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_SUBS_GL_ACTIVE_DAY --class com.cmiot.WriteToDB $statsJar $dataDate STATS_SUBS_GL_ACTIVE_DAY
}

#load data to alluxio
/alluxio/alluxio-1.4.0/bin/alluxio fs load /stats/data/prod/STATS_EC_FLOW_DAY.parquet
/alluxio/alluxio-1.4.0/bin/alluxio fs load /stats/data/prod/STATS_EC_GROUP_FLOW_DAY.parquet