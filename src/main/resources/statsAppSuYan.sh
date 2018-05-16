numExecutors="20"

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

set -x


numExecutors="20"
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutors --name StatsSuYanApp --class com.cmiot.app.StatsSuYanApp $statsJar $statsDate
set -x
numExecutorsWrite="3"
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_INDUSTRY_USERS --class com.cmiot.WriteToDB $statsJar $dataDate STATS_INDUSTRY_USERS
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutorsWrite --name STATS_INDUSTRY_GPRSFLOW --class com.cmiot.WriteToDB $statsJar $dataDate STATS_INDUSTRY_GPRSFLOW
