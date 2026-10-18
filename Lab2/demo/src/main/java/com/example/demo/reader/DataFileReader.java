package com.example.demo.reader;

import com.example.demo.model.PersonRecord;
import java.util.List;

public interface DataFileReader {

    List<PersonRecord> readRecords();

    String getFormat();
}
