package miu.bdt.producer.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import miu.bdt.CommonUtils;
import miu.bdt.Constant;
import miu.bdt.dto.weather.Weather;
import miu.bdt.dto.weather.WeatherData;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherService {
	private static WeatherService INSTANCE = null;
    private static final OkHttpClient client = new OkHttpClient();
    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);
    private static final Gson gson = new Gson();
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.HIVE_TIMESTAMP_FORMAT);
    private static final Set<String> invalidZipcodes = new HashSet<>();
    
    public static WeatherService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WeatherService();
        }
        return INSTANCE;
    }

    private WeatherService() {
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
    
    public Weather getWeatherData(String zipcode) {
        Request request = new Request.Builder()
                .url("https://weatherapi-com.p.rapidapi.com/current.json?q=" + zipcode)
                .get()
                .addHeader("X-RapidAPI-Key", "35ae57a75dmshfa432d963090737p1251cfjsn78a4b5dbd63a")
                .addHeader("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
                .build();
        Response response = null;
        if (invalidZipcodes.contains(zipcode)) {
            return null;
        }
        try {
            response = client.newCall(request).execute();
            if (response.code() == 200) {
                String body = Objects.requireNonNull(response.body()).string();
                WeatherData dto = gson.fromJson(body, WeatherData.class);
                return new Weather(zipcode, dto, simpleDateFormat.format(new Date()));
            } else {
                invalidZipcodes.add(zipcode);
                log.warn("GET Weather data by zip " + zipcode + " " + Objects.requireNonNull(response.body()).string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    public void publishData(String topic, List<Weather> weathers) {

    	KafkaProducer<String, String> producer = this.getProducer();
    	
    	ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic,
                String.valueOf(System.currentTimeMillis()),
                gson.toJson(weathers, new TypeToken<List<Weather>>() {
                }.getType()));
    	
        // send data - asynchronous
        producer.send(producerRecord, (recordMetadata, e) -> {
            if (e == null) {
                // the record was successfully sent
                log.info("Sent new metadata. \n" +
                        "Topic:" + recordMetadata.topic() + "\n" +
                        "Key:" + producerRecord.key() + "\n" +
                        "Value:" + producerRecord.value() + "\n" +
                        "Partition: " + recordMetadata.partition() + "\n" +
                        "Offset: " + recordMetadata.offset() + "\n" +
                        "Timestamp: " + recordMetadata.timestamp());
            } else {
                log.error("Error while publishing data", e);
            }
        });
        
        // flush and close
        producer.flush();
        producer.close();
    }
}
