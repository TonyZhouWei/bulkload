
appVersion=3.2.2
sparkVersion="2"
master="yarn"
deployMode="cluster"
queue="bigdata"
releasePath="/home/stats/release"
statsJar=`ls $releasePath/bigdata-stat-${appVersion}-jar-with-dependencies.jar`
sourceTable="CM_SUBS_SUBSCRIBER,CM_SUBS_SUBSCRIBER_DISUSER,SA_DB_PROVINCE,PC_PROD_COMMINFO,PC_PROD_GPRSSMSDEF,CM_SUBS_ECPRODSUBSINFO,CM_SUBS_ECPRODUCT,SA_DB_PROVINCESWITCH,CM_CU_CUSTOMER,CM_CU_ECUSTOMER,APP_INFO_PLAT,CM_SUBS_PRODSERVICE,SA_SR_OPERGROUP,SA_SR_OPER,SA_DB_DICTITEM,CM_GROUP_MANAGE"
numExecutors="6"
set -x
time SPARK_MAJOR_VERSION=$sparkVersion spark-submit --queue $queue --master $master --deploy-mode $deployMode --num-executors $numExecutors --class com.cmiot.FormatFile $statsJar $sourceTable

time sqoop import -D mapred.job.queue.name=$queue --connect jdbc:oracle:thin:@192.168.10.7:1521:iotdb1 --username iot_app_read --password M2mcq49 --target-dir hdfs://iotnamencluster/stats/data/live/cmSubsProduct --table CM_SUBS_PRODUCT --columns PROVINCEID,SUBSID,PRODID,STARTDATE,STATUS,PRODINSTID,PRODINSTEXPTIME,PKGPRODID,STATUSDATE --as-parquetfile -m 1  --delete-target-dir

time spark-shell --master $master --name CAST_PRODUCT --queue $queue  --executor-memory 3G --num-executors 20 << EOF
sqlContext.sql("select * from parquet.\`hdfs://iotnamencluster/stats/data/live/cmSubsProduct\`").selectExpr("PROVINCEID","CAST(SUBSID as decimal(20,0)) as SUBSID","PRODID","CAST(from_unixtime(STARTDATE/1000,'yyyy-MM-dd HH:mm:ss') as timestamp) as STARTDATE","STATUS","PRODINSTID"," CAST(from_unixtime(PRODINSTEXPTIME/1000,'yyyy-MM-dd HH:mm:ss') as timestamp) as PRODINSTEXPTIME","PKGPRODID"," CAST(from_unixtime(STATUSDATE/1000,'yyyy-MM-dd HH:mm:ss') as timestamp) as STATUSDATE").repartition(200).write.mode("overwrite").parquet("hdfs://iotnamencluster/stats/data/live/cmSubsProduct.parquet")
EOF

