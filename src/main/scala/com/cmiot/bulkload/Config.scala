package com.cmiot.bulkload

object Config extends Serializable {
  private val isInner = true
  var HBase_Parent = ""
  var HBase_Quorum = ""
  var HBase_ClientPort = ""
  var HBase_TableName = ""
  var HBase_ColumnFamily = ""
  var Output_Path = ""
  var HDFS_MASTER = ""

  /*
    def getQuorum(): String = {
      var ret = ""
      if (isInner)
        ret = "10.1.108.65,10.1.108.68,10.1.108.70"
      else
        ret = "192.168.156.105,192.168.156.106,192.168.156.107"

      ret
    }

    def getOutputPath(): String = {
      var ret = ""
      if (isInner)
        ret = "hdfs://slave1:8020/test"
      else
        ret = "hdfs://master:8020/test"

      ret
    }
    */
}
