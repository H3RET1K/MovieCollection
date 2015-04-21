package com.datadynamic.server;

import org.apache.log4j.Logger;

public class ServerLog {
	private static Logger logger;
	static {
		logger = Logger.getLogger(ServerLog.class);
	}
	public static String error(String msg) {		
		String logID = java.util.UUID.randomUUID().toString();
		logger.error(logID + " -:- " + msg);
		return logID;
	}
	public static String warn(String msg) {		
		String logID = java.util.UUID.randomUUID().toString();
		logger.warn(logID + " -:- " + msg);
		return logID;
	}
	public static String debug(String msg) {		
		String logID = java.util.UUID.randomUUID().toString();
		logger.debug(logID + " -:- " + msg);
		return logID;
	}
}