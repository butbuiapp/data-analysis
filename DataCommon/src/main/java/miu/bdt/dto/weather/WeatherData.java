package miu.bdt.dto.weather;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Data
public class WeatherData {
    private Location location;
    private Current current;
	   
    
}
