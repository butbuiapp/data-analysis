package miu.bdt.consumer.weather;

import miu.bdt.Constant;

public class WeatherConsumerApplication {

	public static void main(String[] args) {
		
		// subscribe to Kafka
		KafkaService.subscribe(Constant.TOPIC_NAME_WEATHER);
		
	}

}
