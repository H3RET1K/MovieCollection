package com.datadynamic.client.remoteservices;

import java.util.ArrayList;

import com.datadynamic.shared.pojos.ActionResponse;
import com.datadynamic.shared.pojos.Movie;
import com.datadynamic.shared.pojos.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MovieServiceAsync {
	void getMovieCollection(AsyncCallback<ArrayList<Movie>> callback) throws IllegalArgumentException;

	void removeMovie(long ID, AsyncCallback<ActionResponse> callback);

	void addMovie(String name, String genre, AsyncCallback<ActionResponse> callback);
	
	void getUsers(AsyncCallback<ArrayList<User>> callback) throws IllegalArgumentException;
	
	void addUser(String username, String role,  AsyncCallback<ActionResponse> callback);
	
	void removeUser(long ID, AsyncCallback<ActionResponse> callback);
}
