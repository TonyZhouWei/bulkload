package com.cmiot.tools

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

class Context private() {
  @transient private val appConf = ConfigFactory.load("app.properties")

  @transient private val sparkConf = new SparkConf

  sparkConf.setIfMissing("spark.app.name", appConf.getString("app.name"))
  sparkConf.setIfMissing("spark.master", appConf.getString("master.url"))
  sparkConf.set("spark.sql.shuffle.partitions", appConf.getString("shuffle.partition"))
  sparkConf.set("spark.testing.memory", "10240000000")
  sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
  sparkConf.set("spark.sql.crossJoin.enabled", "true")
  //sparkConf.set("spark.kryo.registrationRequired", "true")
  //TODO fix Class is not registered: byte[][]
  sparkConf.registerKryoClasses(Array(classOf[Array[org.apache.spark.sql.catalyst.InternalRow]],
    classOf[Array[org.apache.spark.sql.types.StructType]],
    classOf[org.apache.spark.sql.types.StructType],
    classOf[Array[org.apache.spark.sql.types.StructField]],
    classOf[org.apache.spark.sql.types.StructField],
    classOf[org.apache.spark.sql.types.Metadata],
    classOf[org.apache.spark.sql.types.DecimalType],
    classOf[org.apache.spark.sql.types.DateType],
    classOf[org.apache.spark.sql.types.StringType],
    classOf[org.apache.spark.sql.types.LongType],
    classOf[org.apache.spark.sql.types.TimestampType],
    classOf[org.apache.spark.sql.catalyst.expressions.GenericInternalRow], classOf[Array[Object]],
    classOf[org.apache.spark.sql.catalyst.expressions.InterpretedOrdering],
    classOf[org.apache.spark.sql.catalyst.expressions.BoundReference],
    classOf[org.apache.spark.sql.catalyst.expressions.SortOrder],
    classOf[org.apache.spark.sql.catalyst.trees.Origin],
    classOf[org.apache.hadoop.conf.Configuration],
    Class.forName("org.apache.hadoop.conf.Configuration"),
    Class.forName("org.apache.spark.sql.execution.columnar.CachedBatch"),
    Class.forName("java.lang.Class"),
    Class.forName("org.apache.spark.sql.types.StringType$"),
    Class.forName("org.apache.spark.sql.types.DecimalType$"),
    Class.forName("org.apache.spark.sql.types.DateType$"),
    Class.forName("org.apache.spark.sql.types.LongType$"),
    Class.forName("org.apache.spark.sql.types.TimestampType$"),
    Class.forName("org.apache.spark.sql.catalyst.expressions.Descending$"),
    Class.forName("org.apache.spark.sql.types.Decimal$DecimalAsIfIntegral$"),
    Class.forName("org.apache.spark.sql.types.Decimal$DecimalIsFractional$"),
    classOf[scala.collection.immutable.Map$EmptyMap$],
    classOf[scala.reflect.ClassTag$$anon$1],
    classOf[scala.math.Ordering$$anon$4],
    classOf[scala.collection.mutable.WrappedArray$ofRef],
    classOf[org.apache.spark.unsafe.types.UTF8String],
    classOf[org.apache.spark.sql.types.ArrayType],
    classOf[org.apache.spark.sql.catalyst.expressions.UnsafeRow]))

  sparkConf.set("spark.sql.warehouse.dir", appConf.getString("spark.sql.warehouse"))

  @transient private val sparkContext = SparkContext.getOrCreate(sparkConf)

  sparkContext.hadoopConfiguration.set("fs.alluxio.impl", "alluxio.hadoop.FileSystem")

  @transient val sqlContext = new SQLContext(sparkContext)

  def getSparkContext = sparkContext

  def getSqlContext = sqlContext
}

case object Context extends Context
