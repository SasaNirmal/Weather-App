package com.amongus.Weather.App.service;

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

        // Check if root has "List" key (actual structure in cities.json)
        if (rootNode.has("List")) {
            arrayNode = rootNode.get("List");
        } else if (rootNode.isArray()) {
            arrayNode = rootNode;
        } else if (rootNode.has("cityCode")) {
            arrayNode = rootNode.get("cityCode");
        } else if (rootNode.has("cityCodes")) {
            arrayNode = rootNode.get("cityCodes");
        }

        if (arrayNode == null || !arrayNode.isArray()) {
            throw new IllegalArgumentException("`List`, `cityCode` or `cityCodes` array missing in `cities.json`.");
        }

        for (JsonNode cityNode : arrayNode) {
            // Handle city objects with "CityCode" field
            if (cityNode.has("CityCode")) {
                JsonNode codeNode = cityNode.get("CityCode");
                if (codeNode.isTextual()) {
                    try {
                        cityCodes.add(Long.parseLong(codeNode.asText()));
                    } catch (NumberFormatException ignored) {
                        // skip invalid entries
                    }
                } else if (codeNode.isNumber()) {
                    cityCodes.add(codeNode.asLong());
                }
            }
            // Handle direct number or string values
            else if (cityNode.isNumber()) {
                cityCodes.add(cityNode.asLong());
            } else if (cityNode.isTextual()) {
                try {
                    cityCodes.add(Long.parseLong(cityNode.asText()));
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
