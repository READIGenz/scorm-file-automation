package com.lh.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesHandler {
    static String strConfPath = "src/main/resources/config.properties";

    public PropertiesHandler() {
    }

    public static String setConfigPath(String value) {
        strConfPath = value;
        return value;
    }

    private static String getProperty(String strPropertyName) {
        String result = null;
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream(strConfPath));
            result = prop.getProperty(strPropertyName);
        } catch (Exception var4) {
            System.out.println("unable to find specified properties file");
            var4.printStackTrace();
        }

        return result;
    }

    public static String getLogsFolder() {
        String path = getProperty("path_logfiles");
        File fileToZip = new File(path);
        if (!fileToZip.exists()) {
            fileToZip.mkdir();
        }

        return path;
    }
}
