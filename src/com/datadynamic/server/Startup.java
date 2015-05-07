package com.datadynamic.server;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;

import com.datadynamic.server.utils.Configuration;
import com.datadynamic.server.utils.DB;
import com.datadynamic.server.utils.ServerLog;

public class Startup implements javax.servlet.ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) { }

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		if(Configuration.getSetting("rebuilddatabaseschema").equals("true")) {
			try {
				DB.rebuildDatabaseSchema();
			} catch (SQLException e) {
				e.printStackTrace();
				ServerLog.error(e.getMessage());
				System.exit(1);
			}
		}
		if(Configuration.getSetting("mergedevdata").equals("true")) {
			try {
				DB.mergeDevData();
			} catch (SQLException e) {
				e.printStackTrace();
				ServerLog.error(e.getMessage());
				System.exit(1);
			}
		}		
	}
}