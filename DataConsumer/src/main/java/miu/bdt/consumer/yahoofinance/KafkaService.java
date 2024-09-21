package miu.bdt.consumer.yahoofinance;

import org.apache.spark.streaming.Duration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import miu.bdt.Constant;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

@SuppressWarnings("deprecation")
public class KafkaService {
	private static final String GROUP_ID = "stock";
	private static final HbaseService hbaseService = HbaseService.getInstance();

	// subcribe to Kafka and consume data
	public static JavaInputDStream<ConsumerRecord<String, String>> subscribe(String topic) {
		//Creating consumer properties
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Constant.KAFKA_SERVER);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        //creating consumer
        JavaStreamingContext streamingContext = new JavaStreamingContext(
                new SparkConf().setAppName("SparkStreaming").setMaster("local[*]"), new Duration(250));
        
    
        //Subscribing
        JavaInputDStream<ConsumerRecord<String, String>> stream =
                KafkaUtils.createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(Collections.singleton(topic), properties)
                );
        
        System.out.println("Started streaming...");
        // consume data from Kafka and save to Hbase
 		stream.foreachRDD(rdd -> rdd.foreach(record -> {
 			System.out.println("received message from Kafka...");
             if (record != null) {
                 hbaseService.insert(record.value());
             }
         }));
        
        streamingContext.start();
        try {
            streamingContext.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return stream;
	}
}
