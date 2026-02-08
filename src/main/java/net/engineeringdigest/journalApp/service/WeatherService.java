package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService
{
    private static final String API_KEY = "2545dcbd8d0541888b634223260402";

    private static final String API = "http://api.weatherapi.com/v1/current.json?key=API_KEY&q=city";

    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeather(String city) {
        String uri = API.replace("API_KEY", API_KEY)
                .replace("city", city);
        ResponseEntity<WeatherResponse> response = restTemplate
                .exchange(uri, HttpMethod.GET, null, WeatherResponse.class);

        return response.getBody();
    }
}
