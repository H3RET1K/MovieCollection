package com.datadynamic.server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.datadynamic.client.remoteservices.UserService;
import com.datadynamic.server.utils.AuthenticationUtils;
import com.datadynamic.server.utils.DB;
import com.datadynamic.server.utils.ServerLog;
import com.datadynamic.shared.Labels;
import com.datadynamic.shared.pojos.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements UserService {
	
	
	@Override
	public ArrayList<User> getUsers() throws IllegalArgumentException {
		if(!AuthenticationUtils.IsAuthenticated(this.getThreadLocalRequest())) {
			return null;
		}
		QueryRunner run = new QueryRunner();
		ResultSetHandler<List<User>> h = new BeanListHandler<User>(User.class);
		List<User> users = null;
		try {
			users = run.query(DB.getConnection(), "SELECT * FROM Users", h);
		} catch (SQLException e) {
			e.printStackTrace();
			ServerLog.error(e.getMessage());
		}
		return new ArrayList<User>(users);
	}
	
	
	@Override
	public ActionResponse removeUser(long ID) {
		if(!AuthenticationUtils.IsAuthenticated(this.getThreadLocalRequest())) {
			return null;
		}
		QueryRunner run = new QueryRunner();
		try {
			run.update(DB.getConnection(), "delete from USERS where ID = ?", ID);
		} catch (SQLException e) {
			e.printStackTrace();
			String logID = ServerLog.error(e.getMessage());
			return new ActionResponse(false, Labels.DB_ERROR + Labels.LABEL_CONCAT + logID);
		}
		return new ActionResponse();
	}	
	
	
	@Override
	public ActionResponse addUser(String username, String role){
		if(!AuthenticationUtils.IsAuthenticated(this.getThreadLocalRequest())) {
			return null;
		}
		QueryRunner run = new QueryRunner();
		ResultSetHandler<User> h = new BeanHandler<User>(User.class);
		try {
			run.insert(DB.getConnection(), "insert into USERS(username, invalidattempts, role, islocked) values (?, ?, ?, ?)", h, username, "0", role, "0");
		} catch (SQLException e) {
			e.printStackTrace();
			String logID = ServerLog.error(e.getMessage());
			return new ActionResponse(false, Labels.DB_ERROR + Labels.LABEL_CONCAT + logID);
		}
		return new ActionResponse();
	}

}
