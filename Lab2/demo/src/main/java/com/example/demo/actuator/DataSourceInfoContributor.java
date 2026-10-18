package com.example.demo.actuator;

import com.example.demo.config.DataFileProperties;
import com.example.demo.reader.DataFileReader;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataSourceInfoContributor implements InfoContributor {

    private final DataFileProperties properties;
    private final DataFileReader reader;

    public DataSourceInfoContributor(
            DataFileProperties properties,
            DataFileReader reader) {
        this.properties = properties;
        this.reader = reader;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("dataSource", Map.of(
                "file", properties.getPath(),
                "format", reader.getFormat(),
                "reader", reader.getClass().getSimpleName(),
                "recordCount", reader.readRecords().size()
        ));
    }
}
