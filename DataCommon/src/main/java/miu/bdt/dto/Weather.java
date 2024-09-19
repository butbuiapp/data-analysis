package miu.bdt.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Weather {

    private String zipcode;
    private String city;
    private float temp;
    private String updatedDate;

    public Weather(String zipcode, WeatherData dto, String updatedDate) {
        this.zipcode = zipcode;
        this.city = dto.getLocation().getName();
        this.temp = dto.getCurrent().getTemp_f();
        this.updatedDate = updatedDate;
    }
}
