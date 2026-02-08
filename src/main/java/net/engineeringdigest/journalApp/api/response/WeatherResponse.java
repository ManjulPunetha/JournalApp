package net.engineeringdigest.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeatherResponse
{
    private Location location;
    private Current current;

    @Data
    public static class Current
    {
        @JsonProperty("temp_c")
        private double temperature;
        @JsonProperty("is_day")
        private int isDay;
        private double uv;
    }

    @Data
    public static class Location
    {
        private String name;
        private String region;
        private String country;
        private String localtime;
    }
}
