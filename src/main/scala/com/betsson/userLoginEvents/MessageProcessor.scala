
package com.betsson.userLoginEvents

import org.apache.spark.sql.SQLContext
import org.apache.spark.rdd.RDD
import org.apache.spark.Accumulator
import org.apache.spark.sql.SaveMode

object MessageProcessor extends Serializable {
  
  val CLASS_NAME = this.getClass

  @transient lazy val log = org.apache.log4j.LogManager.getLogger(CLASS_NAME)

  def processStream(accumulator: Accumulator[Long], sqlContext: SQLContext, rdd: RDD[String]) {
    
    try{
      
      if (!rdd.isEmpty() && null!=sqlContext) {
        
        
        
       
        sqlContext.read.json(rdd)
        .select("customerid", "brandname", "timestamp","issuccessful")
        .write
        .format("org.apache.spark.sql.cassandra")
        .options(Map("table" -> "userlogins", "keyspace" -> "betsson"))
        .mode(SaveMode.Append)
        .save()
        
        
        
        
       /* customer.registerTempTable("UserLogins")
        
       sqlContext.sql("insert into table UserLogins values(\"00002c28-7599-11e1-8028‐1cc1dee5ebd\",\"TestBrand\",\"2016‐05‐31T09:06:33.303\",true)")
       
        val cust = sqlContext.sql("SELECT CustomerId FROM UserLogins group by CustomerId having count(CustomerId) > 2")
        println("*****************************"+cust.show() + "**********************************")
        
        cust.foreach { x => FailedEventPublish.send(x.getAs("CustomerId")) }
        */
        
      }
      
    } catch {
      case e: Exception =>
        e.printStackTrace()
        log.debug("Bad message received, Ececption is :::::" + e.printStackTrace())
        rdd.foreachPartition { iter =>
          iter.foreach {
            case (msg) =>
              accumulator.add(1)
              log.debug("Bad Message is:::::" + msg)

          }
        }
    }

      
    
  }
  
  
  
}



