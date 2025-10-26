package com.amongus.Weather.App.controller;

import com.amongus.Weather.App.model.WeatherResponse;
import com.amongus.Weather.App.service.CityService;
import com.amongus.Weather.App.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WeatherController {

    @Autowired
    private CityService cityService;

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/")
    public String getWeather(Model model) {
        List<Long> cityCodes = cityService.getCityCodes();
        List<WeatherResponse> weatherList = cityCodes.stream()
                .map(weatherService::getWeather)
                .collect(Collectors.toList());
        model.addAttribute("weatherList", weatherList);
        return "weather";
    }
}