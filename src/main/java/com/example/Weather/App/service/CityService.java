package com.example.Weather.App.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CityService {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final List<Long> cityCodes = new ArrayList<>();

    public CityService() {
        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("cities.json")) {
            if (inputStream == null) {
                throw new IllegalStateException("`cities.json` file not found on classpath.");
            }
            JsonNode rootNode = OBJECT_MAPPER.readTree(inputStream);
            loadCityCodes(rootNode);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load city codes from `cities.json`.", e);
        }
    }

    private void loadCityCodes(JsonNode rootNode) {
        if (rootNode == null) {
            throw new IllegalArgumentException("Root JSON node is null.");
        }

        JsonNode arrayNode = null;
        if (rootNode.isArray()) {
            arrayNode = rootNode;
        } else if (rootNode.has("cityCode")) {
            arrayNode = rootNode.get("cityCode");
        } else if (rootNode.has("cityCodes")) {
            arrayNode = rootNode.get("cityCodes");
        }

        if (arrayNode == null || !arrayNode.isArray()) {
            throw new IllegalArgumentException("`cityCode` or `cityCodes` array missing in `cities.json`.");
        }

        for (JsonNode codeNode : arrayNode) {
            if (codeNode.isNumber()) {
                cityCodes.add(codeNode.asLong());
            } else if (codeNode.isTextual()) {
                try {
                    cityCodes.add(Long.parseLong(codeNode.asText()));
                } catch (NumberFormatException ignored) {
                    // skip invalid entries
                }
            }
        }
    }

    public List<Long> getCityCodes() {
        return Collections.unmodifiableList(new ArrayList<>(cityCodes));
    }
}
