package com.example.demo.reader;

import com.example.demo.config.DataFileProperties;
import com.example.demo.model.PersonRecord;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Profile("prod")
@ConditionalOnExpression("'${app.data.format:json}' == 'json'")
public class JsonDataFileReader implements DataFileReader {

    private final DataFileProperties properties;
    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    public JsonDataFileReader(
            DataFileProperties properties,
            ObjectMapper objectMapper,
            ResourceLoader resourceLoader) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public List<PersonRecord> readRecords() {
        Resource resource =
                resourceLoader.getResource(properties.getPath());

        try (var inputStream = resource.getInputStream()) {
            return objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<PersonRecord>>() {});
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not read JSON file: "
                            + properties.getPath(), e);
        }
    }

    @Override
    public String getFormat() {
        return "json";
    }
}
