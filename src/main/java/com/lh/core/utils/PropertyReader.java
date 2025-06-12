package com.lh.core.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader{

    public static String getValueForKey(String key, String filePath) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException e) {
            //e.printStackTrace();
            throw e;
        }
        return properties.getProperty(key);
    }
}

