
package com.betsson.userLoginEvents

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, ProducerConfig}
import java.util.HashMap


object FailedEventPublish extends Serializable {


  
  val kafkaParams = "localhost:9092"

  
  val props = new HashMap[String, Object]()
      props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaParams)
      props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
               "org.apache.kafka.common.serialization.StringSerializer")
      props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer")
                
                
    val producer = new KafkaProducer[String, String](props)
    

    def send(value: String): Unit ={
    
    println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$4"+value+"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$44")
    
    producer.send(new ProducerRecord("ConsumerTopic","{\"CustomerId\":\""+value+"\",\"EventName\":\"Failed3ConsecutiveLogins\",\"Timestamp\":\""+System.currentTimeMillis+"\",}"))
  }
}




