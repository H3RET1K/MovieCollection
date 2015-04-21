package com.datadynamic.server;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.h2.Driver;

@SuppressWarnings("unused")
public final class DB {	
	private static DataSource ds = null;
    
    static {        
        Properties prop = new Properties();
        try {
        	// load from CLASSPATH
        	prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("DB.properties"));
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("Cannot read DB.properties file from classpath");
            System.exit(1); 
        }
                       
        String jdbcDriver = prop.getProperty("jdbc_driver");   
        String jdbcUrl = prop.getProperty("jdbc_url"); 
        String username = prop.getProperty("username");                 
        String password = prop.getProperty("password");

        try {
			Class.forName("org.h2.Driver");
	        HikariConfig config = new HikariConfig();
	        config.setDataSourceClassName(jdbcDriver);
	        config.setConnectionTestQuery("VALUES 1");
	        config.addDataSourceProperty("URL", jdbcUrl);
	        config.addDataSourceProperty("user", username);
	        config.addDataSourceProperty("password", password);
	        config.setJdbcUrl(jdbcUrl);
	        config.setUsername(username);
	        config.setPassword(password);        
	        ds = new HikariDataSource(config);
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
			System.out.println("Cannot get connection pool.");
            System.exit(1); 			
		}
    }

    private DB() { }
    
    static public DataSource getDS() {
    	return ds;
    }
    
    static public Connection getConnection() throws SQLException {
    	return ds.getConnection();
    }    
}
