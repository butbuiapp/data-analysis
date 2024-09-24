package miu.bdt.producer.yahoofinance;

import java.util.Properties;

import miu.bdt.CommonUtils;
import miu.bdt.Constant;
import miu.bdt.dto.yahoofinance.Ticker;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class KafkaService {
	private static final Logger log = LoggerFactory.getLogger(KafkaService.class);
	private static final Gson gson = new Gson();
	private static KafkaService INSTANCE = null;
	private KafkaProducer<String, String> producer = null;
	
	private KafkaService() {
		producer = getProducer();
	}
	
	public static KafkaService getInstance() {
		if (INSTANCE == null) {
            INSTANCE = new KafkaService();
        }
        return INSTANCE;
	}

	private KafkaProducer<String, String> getProducer() {
        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, CommonUtils.getKafkaServer());
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, Constant.MESSAGE_SIZE);
        return new KafkaProducer<>(properties);
    }
	
	public void publishData(String topic, Ticker ticker) {
    	//KafkaProducer<String, String> producer = this.getProducer();
    	
    	ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic,
                String.valueOf(System.currentTimeMillis()),
                gson.toJson(ticker, new TypeToken<Ticker>() {
                }.getType()));
    	
        // send data - asynchronous
        producer.send(producerRecord, (recordMetadata, e) -> {
            if (e == null) {
                // the record was successfully sent
//                log.info("Ticker sent. \n" +
//                        "Topic:" + recordMetadata.topic() + "\n" +
//                        "Key:" + producerRecord.key() + "\n" +
//                        "Value:" + producerRecord.value() + "\n" +
//                        "Partition: " + recordMetadata.partition() + "\n" +
//                        "Offset: " + recordMetadata.offset() + "\n" +
//                        "Timestamp: " + recordMetadata.timestamp());
            } else {
                log.error("Error while publishing data", e);
            }
        });
        
        // flush and close
//        producer.flush();
//        producer.close();
    }
}
