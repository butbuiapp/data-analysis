package miu.bdt.producer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import miu.bdt.Constant;
import miu.bdt.dto.Weather;

public class ProducerApplication {
	private static final ProducerService service = ProducerService.getInstance();

	public static void main(String[] args) {
		System.out.println("Producer....");
		process(Arrays.asList("77479", "52557"));
	}
	
	
	static String process(List<String> zipcodes) {
        System.out.println("PROCESSING " + zipcodes.size() + " RECORDS!!!!!");
        
        List<Weather> weathers = new ArrayList<>();
        
        for (String zip : zipcodes) {
            Weather weather = service.getWeatherData(zip);
            if (weather != null) {
                weathers.add(weather);
                System.out.println(weather);
            }
        }

        // publish data        
        service.publishData(Constant.TOPIC_NAME, weathers);
        
        System.out.println("PROCESSED " + zipcodes.size() + " RECORDS!!!!!");
        return "";
    }

}
