package com.example.demo.reader;

import com.example.demo.config.DataFileProperties;
import com.example.demo.model.PersonRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Profile("dev")
@ConditionalOnExpression("'${app.data.format:csv}' == 'csv'")
public class CsvDataFileReader implements DataFileReader {

    private final DataFileProperties properties;
    private final ResourceLoader resourceLoader;

    public CsvDataFileReader(
            DataFileProperties properties,
            ResourceLoader resourceLoader) {
        this.properties = properties;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public List<PersonRecord> readRecords() {
        Resource resource =
                resourceLoader.getResource(properties.getPath());

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        resource.getInputStream(),
                        StandardCharsets.UTF_8))) {

            return reader.lines()
                    .skip(1)
                    .filter(line -> !line.isBlank())
                    .map(line -> line.split(",", -1))
                    .map(columns -> {
                        if (columns.length != 3) {
                            throw new IllegalArgumentException(
                                    "Expected 3 CSV columns");
                        }

                        return new PersonRecord(
                                Integer.parseInt(columns[0].trim()),
                                columns[1].trim(),
                                columns[2].trim());
                    })
                    .toList();

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not read CSV file: "
                            + properties.getPath(), e);
        }
    }

    @Override
    public String getFormat() {
        return "csv";
    }
}
