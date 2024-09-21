package miu.bdt.producer.weather;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import miu.bdt.Constant;
import miu.bdt.dto.weather.Weather;

public class WeatherApplication {
	private static final WeatherService service = WeatherService.getInstance();

	private static final int SIZE_CHUNK = 20;
	private static final ExecutorService executor = Executors.newFixedThreadPool(5);

	public static void main(String[] args) {
		System.out.println("Producer....");
		
		getWeatherData(Arrays.asList("77479", "52557"));
	}
	
	
	static String getWeatherData(List<String> zipcodes) {
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
        service.publishData(Constant.TOPIC_NAME_WEATHER, weathers);
        
        System.out.println("PROCESSED " + zipcodes.size() + " RECORDS!!!!!");
        return "";
    }

}
