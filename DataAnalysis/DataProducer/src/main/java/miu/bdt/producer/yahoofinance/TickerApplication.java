package miu.bdt.producer.yahoofinance;

import java.net.URI;
import java.net.URISyntaxException;

public class TickerApplication {
	
	public static void main(String[] args) throws InterruptedException {
		try {
			TickerService client = new TickerService(new URI("wss://streamer.finance.yahoo.com/"));
			client.connectBlocking(); // Wait for connection to be established
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
