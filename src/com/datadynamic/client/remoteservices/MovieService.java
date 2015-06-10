package com.datadynamic.client.remoteservices;

import java.util.ArrayList;

import com.datadynamic.shared.pojos.ActionResponse;
import com.datadynamic.shared.pojos.Movie;
import com.datadynamic.shared.pojos.User;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("movie")
public interface MovieService extends RemoteService {
	ArrayList<Movie> getMovieCollection() throws IllegalArgumentException;
	ActionResponse removeMovie(long ID);
	ActionResponse addMovie(String name, String genre);
	
	ArrayList<User> getUsers() throws IllegalArgumentException;
	ActionResponse addUser(String username, String role);
	ActionResponse removeUser(long ID);
}
