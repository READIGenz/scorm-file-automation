package com.lh.core.utils;

import com.lh.core.config.PropertiesHandler;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ErrorCodeReader {

    public static String getErrorCode(String errorcode) {
        String result = null;
        Properties prop = new Properties();

        try {
            prop.load(Files.newInputStream(Paths.get(PropertiesHandler.setConfigPath("ErrorConfPath"))));
            result = prop.getProperty(errorcode);
        } catch (Exception var4) {
            System.out.println("unable to find specified properties file");
            var4.printStackTrace();
        }

        return result;
    }
}

