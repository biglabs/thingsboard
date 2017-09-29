package com.biglabs.iot.tsexportservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class ObjectToJsonFile {

    @Value("${export.outputFolder}")
    private String outputFolder;

    public void writeJsonFile(Object obj, String fileName) {
        File file = new File(outputFolder + fileName);
        try {
            // Serialize Java object info JSON file.
            new ObjectMapper().writeValue(file, obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
