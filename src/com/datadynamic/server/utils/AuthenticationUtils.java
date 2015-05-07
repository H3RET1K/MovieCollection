package com.datadynamic.server.utils;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationUtils {
	public static Boolean IsAuthenticated(HttpServletRequest request)
	{
		if(request.getSession(false) != null) {
            if(request.getSession(false).getAttribute("login.isDone") == "true") {
            	return true;
            }
		}
		return false;
	}
}
