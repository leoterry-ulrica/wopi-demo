package com.dist.common;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

public class GlobalConfig {
    private static Properties properties = new Properties();
    static {
        ClassPathResource cr = new ClassPathResource("global.properties");
        try {
            properties.load(cr.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static Properties getProperties() {
        return properties;
    }

    public static void setProperties(Properties properties) {
        GlobalConfig.properties = properties;
    }
    
    public static boolean isDebugModel(){
        String model = get("mode");
        if("debug".equals(model)){
            return true;
        }
        return false;
    }
    
}
