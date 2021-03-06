package com.datadynamic.server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.datadynamic.client.remoteservices.MovieService;
import com.datadynamic.server.utils.AuthenticationUtils;
import com.datadynamic.server.utils.DB;
import com.datadynamic.server.utils.ServerLog;
import com.datadynamic.shared.Labels;
import com.datadynamic.shared.pojos.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


@SuppressWarnings("serial")
public class MovieServiceImpl extends RemoteServiceServlet implements MovieService {	
	
	
	@Override
	public ArrayList<Movie> getMovieCollection() throws IllegalArgumentException {
		if(!AuthenticationUtils.IsAuthenticated(this.getThreadLocalRequest())) {
			return null;
		}
		QueryRunner run = new QueryRunner();
		ResultSetHandler<List<Movie>> h = new BeanListHandler<Movie>(Movie.class);
		List<Movie> movies = null;
		try {
			movies = run.query(DB.getConnection(), "SELECT * FROM Movies", h);
		} catch (SQLException e) {
			e.printStackTrace();
			ServerLog.error(e.getMessage());
		}
		return new ArrayList<Movie>(movies);
	}
		

	@Override
	public ActionResponse removeMovie(long ID) {
		if(!AuthenticationUtils.IsAuthenticated(this.getThreadLocalRequest())) {
			return null;
		}	
		QueryRunner run = new QueryRunner();
		try {
			run.update(DB.getConnection(), "delete from MOVIES where ID = ?", ID);
		} catch (SQLException e) {
			e.printStackTrace();
			String logID = ServerLog.error(e.getMessage());
			return new ActionResponse(false, Labels.DB_ERROR + Labels.LABEL_CONCAT + logID);
		}		
		return new ActionResponse();
	}
	

	@Override
	public ActionResponse addMovie(String name, String genre) {
		if(!AuthenticationUtils.IsAuthenticated(this.getThreadLocalRequest())) {
			return null;
		}
		QueryRunner run = new QueryRunner();
		ResultSetHandler<Movie> h = new BeanHandler<Movie>(Movie.class);
		try {						
			run.insert(DB.getConnection(), "insert into MOVIES(name, genre) values (?, ?)", h, name, genre);
		} catch (SQLException e) {
			e.printStackTrace();
			String logID = ServerLog.error(e.getMessage());
			return new ActionResponse(false, Labels.DB_ERROR + Labels.LABEL_CONCAT + logID);
		}
		return new ActionResponse();
	}
	
}
