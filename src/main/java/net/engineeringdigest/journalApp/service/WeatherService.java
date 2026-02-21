package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService
{
    @Value("${weather.api.key}")
    private static final String API_KEY = "2ab68e81f9cb41e5b2f140023262102";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    public WeatherResponse getWeather(String city) {
        String uri = appCache.cache.get("API_URI").replace("<API_KEY>", API_KEY)
                .replace("<city>", city);
        ResponseEntity<WeatherResponse> response = restTemplate
                .exchange(uri, HttpMethod.GET, null, WeatherResponse.class);

        return response.getBody();
    }
}
