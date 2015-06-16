package com.datadynamic.client.remoteservices;

import com.google.gwt.core.client.GWT;

public class Services {
	public static MovieServiceAsync movieService = GWT.create(MovieService.class);
	public static AuthenticationServiceAsync authenticationService = GWT.create(AuthenticationService.class);
	public static UserServiceAsync userService = GWT.create(UserService.class);
}
