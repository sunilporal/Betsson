1-> Used Direct Approach with no Receivers, With KafkaUtils.createDirectStream, Spark Streaming will create as many RDD partitions as there are Kafka partitions to consume, which will all read data from Kafka in parallel. So there is a one-to-one mapping between Kafka and RDD partitions, which is easier to understand and tune.
	I have used Cassandra table as I/O system where i can save the events and perform operations according to the requirement. 

2-> Used Meta Data Checkpoints to recovering from failures of the driver running the application and keep track of last known processed TXNs.	

3-> Exceptions in logs can be monitored using Spark Master UI, it gives application level log details for stdout and stderr.

4-> I want to deploy this in cluster mode.


Descions taken and main areas of focus.

1 -> DirectStream Approach with no receivers and writeAheadLogs.
2 -> CheckPointing.
3 -> Integratind spark with cassandra to manupulate the data. 

System to store data:

1-> used Cassandra to store data, because cassandra comes with open source spark connector and it can be easily integrated with spark.
