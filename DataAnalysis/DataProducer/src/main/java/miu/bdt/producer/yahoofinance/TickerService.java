package miu.bdt.producer.yahoofinance;

import java.net.URI;
import java.util.Base64;
import java.util.List;

import miu.bdt.Constant;
import miu.bdt.dto.yahoofinance.Ticker;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.google.protobuf.InvalidProtocolBufferException;

public class TickerService extends WebSocketClient {
    private List<String> tickers = null;
    private static final KafkaService kafkaService = KafkaService.getInstance();
    
    public TickerService(URI serverUri) {
		super(serverUri);
		
		// read tickers from csv file
		tickers = FileService.getTickers();
	}

    @Override
	public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket connection closed. Reason: " + reason);
    }

	@Override
	public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
    }

	@Override
	public void onMessage(String message) {
        // Decode base64-encoded protobuf message
        byte[] messageBytes = Base64.getDecoder().decode(message);
        try {
            // Parse Protobuf message
            Yaticker.yaticker tickerMessage = Yaticker.yaticker.parseFrom(messageBytes);
            //System.out.println("Ticker data: " + tickerMessage);
            
            // publish to Kafka
            kafkaService.publishData(Constant.TOPIC_NAME_YAHOO_FINANCE, map2Ticker(tickerMessage));
            
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void onOpen(ServerHandshake handshakedata) {
        System.out.println("WebSocket connection established.");
        
        // Subscribing to a list of tickers - TOP 100 companies        
        String subscribeMessage = "{\"subscribe\": [\"" + String.join("\", \"", tickers) + "\"]}";
        send(subscribeMessage);
    }
	
	private Ticker map2Ticker(Yaticker.yaticker yTicker) {
		Ticker ticker = Ticker.builder()
				.id(yTicker.getId())
				.price(yTicker.getPrice())
				.time(yTicker.getTime())
				.exchange(yTicker.getExchange())
				.quoteType(yTicker.getQuoteType().name())
				.marketHours(yTicker.getMarketHours().name())
				.changePercent(yTicker.getChangePercent())
				.change(yTicker.getChange())
				.build();		
		return ticker;
	}
}
