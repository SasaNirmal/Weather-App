package com.example.Weather.App.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class CityService {
    private final List<Long> cityCodes = new ArrayList<>();

    public CityService() {
        try (InputStream inputStream = getClass().getResourceAsStream("/cityCodes.json")) {
            if (inputStream != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(inputStream);
                loadCityCodes(rootNode);
            } else {
                throw new IllegalArgumentException("cityCodes.json file not found.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load city codes.", e);
        }
    }

    private void loadCityCodes(JsonNode rootNode) {
        if (rootNode != null) {
            JsonNode cityCodeNode = rootNode.get("cityCode");
            if (cityCodeNode != null && cityCodeNode.isArray()) {
                for (JsonNode codeNode : cityCodeNode) {
                    cityCodes.add(codeNode.asLong());
                }
            } else {
                throw new IllegalArgumentException("City code is missing or invalid in the JSON data.");
            }
        } else {
            throw new IllegalArgumentException("Root JSON node is null.");
        }
    }

    public List<Long> getCityCodes() {
        return cityCodes;
    }
}
