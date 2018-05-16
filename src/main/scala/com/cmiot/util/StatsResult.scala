package com.cmiot.util

import org.apache.spark.sql.DataFrame

/**
  * Created by hp on 2017/9/7.
  */
class StatsResult(argResult:DataFrame, argPartitionKey:String, argPartitionVal:String) extends Serializable {
    val result = argResult
    val partitionKey = argPartitionKey
    val partitionVal = argPartitionVal
}
