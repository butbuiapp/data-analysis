package miu.bdt.consumer.weather;

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

public class NewKafkaService {
	private static final String GROUP_ID = "stock";

//	public static JavaInputDStream<ConsumerRecord<String, String>> subscribe(String topic) {
//		 // Create Spark session
//        SparkSession spark = SparkSession.builder()
//                .appName("KafkaStructuredStreaming")
//                .master("local[*]")  // For local testing
//                .getOrCreate();
//	}
}
