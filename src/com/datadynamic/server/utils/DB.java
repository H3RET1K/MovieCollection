package com.datadynamic.server.utils;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.h2.Driver;

@SuppressWarnings("unused")
public final class DB {	
	private static DataSource ds = null;
    
    static {        
        /*Properties prop = new Properties();
        try {
        	// load from CLASSPATH
        	prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("DB.properties"));
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("Cannot read DB.properties file from classpath");
            System.exit(1); 
        }*/
        
        String jdbcDriver = Configuration.getSetting("jdbc_driver");   
        String jdbcUrl = Configuration.getSetting("jdbc_url"); 
        String username = Configuration.getSetting("username");                 
        String password = Configuration.getSetting("password");

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
	        PreparedStatement testStmt =
                ds.getConnection().prepareStatement("VALUES 1");
	        testStmt.execute();
		} catch (Exception e) {			
			e.printStackTrace();
			System.out.println("Cannot get connection pool.");
            System.exit(1); 			
		}
    }

    private DB() { }
    
    static public DataSource getDS() {
    	return ds;
    }
    
    @SuppressWarnings("resource")
	static public void rebuildDatabaseSchema() throws SQLException {
    	QueryRunner run = new QueryRunner();
    	String text = new Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream("BuildSchema.sql"), "UTF-8").useDelimiter("\\A").next();
    	run.update(DB.getConnection(), text);
    }
    
    @SuppressWarnings("resource")
	static public void mergeDevData() throws SQLException {
    	QueryRunner run = new QueryRunner();
    	String text = new Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream("DevData.sql"), "UTF-8").useDelimiter("\\A").next();
    	run.update(DB.getConnection(), text);
    }    
    
    static public Connection getConnection() throws SQLException {
    	return ds.getConnection();
    }    
}
