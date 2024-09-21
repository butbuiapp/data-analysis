package miu.bdt.dto.yahoofinance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticker {

	private String id;
	private float price;
	private long time;
	private String exchange;
	private String quoteType;
	private String marketHours;
	private float changePercent;
	private float change;
	
}
