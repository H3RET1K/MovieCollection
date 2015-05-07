package com.datadynamic.client.remoteservices;

import com.datadynamic.shared.pojos.ActionResponse;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("authentication")
public interface AuthenticationService extends RemoteService {
	ActionResponse isLoggedIn();
	ActionResponse login(String username, int token);
	ActionResponse logout();
}
