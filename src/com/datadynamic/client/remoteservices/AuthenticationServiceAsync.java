package com.datadynamic.client.remoteservices;

import com.datadynamic.shared.pojos.ActionResponse;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthenticationServiceAsync {
	void isLoggedIn(AsyncCallback<ActionResponse> callback);
	void login(String username, int token, AsyncCallback<ActionResponse> callback);
	void logout(AsyncCallback<ActionResponse> callback);
}
