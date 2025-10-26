package com.amongus.Weather.App.service;

import com.amongus.Weather.App.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Cacheable(value = "weatherCache", key = "#cityId")
    public WeatherResponse getWeather(Long cityId) {
        String url = "https://api.openweathermap.org/data/2.5/weather?id=" + cityId + "&appid=" + apiKey;
        return restTemplate.getForObject(url, WeatherResponse.class);
    }
}
