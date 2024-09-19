# data-analysis
Big Data Technology Final Project (Technologies used: Hadoop, Kafka, Spark Streaming, HBase, Java, Grafana)

## Kafka
  Download: https://downloads.apache.org/kafka/3.8.0/kafka_2.13-3.8.0.tgz

  Start Kafka: bin/kafka-server-start.sh -daemon config/server.properties

## Project Architecture
  DataAnalysis
  
     |__ DataCommon: for common parts (model, config, ...)
     |__ DataProducer: call APIs to get data, then publish data to a message queue (Kafka)
     |__ DataConsumer: subcribe to the topic in Kafka, get data and store in HBase
     |__ DataVisualization: get data from HBase and create graphs using Grafana

