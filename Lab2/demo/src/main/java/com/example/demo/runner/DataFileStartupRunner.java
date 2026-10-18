package com.example.demo.runner;

import com.example.demo.config.DataFileProperties;
import com.example.demo.model.PersonRecord;
import com.example.demo.reader.DataFileReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
public class DataFileStartupRunner implements CommandLineRunner {

    private final DataFileReader reader;
    private final DataFileProperties properties;
    private final Environment environment;

    public DataFileStartupRunner(
            DataFileReader reader,
            DataFileProperties properties,
            Environment environment) {
        this.reader = reader;
        this.properties = properties;
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        List<PersonRecord> records = reader.readRecords();

        System.out.println();
        System.out.println("===== FILE DATA LOADED =====");
        System.out.println("Active profiles: "
                + String.join(", ", environment.getActiveProfiles()));
        System.out.println("Configured file: " + properties.getPath());
        System.out.println("Format: " + reader.getFormat());
        System.out.println("Reader: " + reader.getClass().getSimpleName());
        System.out.println("Number of records: " + records.size());
        System.out.println("Priority test value: "
                + environment.getProperty("app.priority.demo"));

        System.out.println("Records:");
        records.forEach(record ->
                System.out.printf(
                        "id=%d, name=%s, department=%s%n",
                        record.id(),
                        record.name(),
                        record.department()));

        System.out.println("============================");
    }
}
