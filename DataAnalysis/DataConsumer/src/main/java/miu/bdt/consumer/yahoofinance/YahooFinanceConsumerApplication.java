package miu.bdt.consumer.yahoofinance;

import miu.bdt.Constant;

public class YahooFinanceConsumerApplication {

	public static void main(String[] args) {
		
		// subscribe to Kafka
		KafkaService.subscribe(Constant.TOPIC_NAME_YAHOO_FINANCE);
		
	}

}
