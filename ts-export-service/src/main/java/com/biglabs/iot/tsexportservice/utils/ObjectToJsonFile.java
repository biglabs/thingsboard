package com.biglabs.iot.tsexportservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ObjectToJsonFile {

    public static void writeJsonFile(Object obj) {
        File file = new File("d:\\artist.json");

        try {
            // Serialize Java object info JSON file.
            new ObjectMapper().writeValue(file, obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
