package com.datadynamic.server.utils;

import java.util.Properties;

public final class Configuration {
	private static final Properties prop = new Properties();
    static {        
        try {
        	// load from CLASSPATH
        	prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("App.properties"));
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("Cannot read DB.properties file from classpath");
            System.exit(1); 
        }
    }
    public Configuration() {}
    
    public static String getSetting(String key) {
    	return prop.getProperty(key);
    }
}
