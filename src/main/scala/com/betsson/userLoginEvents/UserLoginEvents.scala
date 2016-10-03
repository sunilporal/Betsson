
package com.betsson.userLoginEvents

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.Accumulator
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.sql.SQLContext
import kafka.serializer.StringDecoder
import net.liftweb.json.JsonDSL._
import net.liftweb.json._

object UserLoginEvents {
  def main(args: Array[String]): Unit = {
    

    
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("UserLoginEvents")
    .set("spark.cassandra.connection.host", "127.0.0.1")
     
     sparkConf.set("spark.streaming.receiver.writeAheadLog.enable", "true")
     
     
     
    val sc = new SparkContext(sparkConf)
    
    val ssc = new StreamingContext(sc, Seconds(5))
    val kafkaParams = Map("metadata.broker.list" -> "localhost:9092")
    
    val topics = Set("CustomerLogins")
    val sqlContext = new SQLContext(sc)
    
 //   ssc.checkpoint("/home/sac/work/KafkaCheckPoint/")
    
    val stream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics).map(_._2)
 //   stream.checkpoint(Seconds(2 * 5))
    
    val accumulator = getInstance(sc)
    
    println("-------------------Test --------------------------------------")
    
    stream.map {
          msg => msg.toString()

        }.foreachRDD(rdd => MessageProcessor.processStream(accumulator, sqlContext, rdd))

    ssc.start()
    println("started now-->> " + compat.Platform.currentTime)
    ssc.awaitTermination()
    
  }
  
  @volatile private var instance: Accumulator[Long] = null

  def getInstance(sc: SparkContext): Accumulator[Long] = {
    if (instance == null) {
      synchronized {
        if (instance == null) {
          instance = sc.accumulator(0L, "badMessageCounter")
        }
      }
    }
    instance
  }
}