package com.amongus.Weather.App.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    private String name;
    private Main main;
    private Weather[] weather;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Main getMain() { return main; }
    public void setMain(Main main) { this.main = main; }
    public Weather[] getWeather() { return weather; }
    public void setWeather(Weather[] weather) { this.weather = weather; }

    public static class Main {
        private double temp;
        // Getters and setters
        public double getTemp() { return temp; }
        public void setTemp(double temp) { this.temp = temp; }
    }

    public static class Weather {
        private String description;
        // Getters and setters
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
