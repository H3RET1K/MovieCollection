package com.datadynamic.client.remoteservices;

import java.util.ArrayList;

import com.datadynamic.shared.pojos.ActionResponse;
import com.datadynamic.shared.pojos.User;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService {	
	ArrayList<User> getUsers() throws IllegalArgumentException;
	ActionResponse addUser(String username, String role);
	ActionResponse removeUser(long ID);
}

