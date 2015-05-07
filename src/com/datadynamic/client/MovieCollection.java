package com.datadynamic.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.datadynamic.client.events.AuthenticationLoginEvent;
import com.datadynamic.client.events.Events;
import com.datadynamic.client.remoteservices.Services;

import com.datadynamic.shared.pojos.*;

public class MovieCollection implements EntryPoint {		
	final private int authenticationTimerMS = 30000;
	private RootPanel entryPointRootPanel = RootPanel.get();
	private MainPanel mainPanel = null;
	
	/*
	Authentication 
	*/
	
	final Timer authCheckTimer = new Timer() {
		@Override
		public void run() {
			verifyAuthentication();
		}
	};
	
	private void verifyAuthentication() {
		Services.authenticationService.isLoggedIn(new AsyncCallback<ActionResponse> () {
			@Override
			public void onFailure(Throwable caught) {
				ErrorDialogBox errBox = new ErrorDialogBox();
				errBox.setNetworkError();
			}

			@Override
			public void onSuccess(ActionResponse result) {				
				if(result.getSuccess() == false) {
					authCheckTimer.cancel();
					if(mainPanel != null) {
						entryPointRootPanel.remove(mainPanel);
					}
					@SuppressWarnings("unused")
					LoginDialogBox loginBox = new LoginDialogBox();
				} else {
					// in case page is refreshed and they are already logged in
					Events.EVENT_BUS.fireEvent(new AuthenticationLoginEvent());								
				}
			}
		});		
	}
	
	public void onModuleLoad() {
		/*
		Authentication 
		*/
		
		// run once, on success of login will kick the auth timer off
		authCheckTimer.run();
		
		/*
		Events
		*/
			
		Events.EVENT_BUS.addHandler(AuthenticationLoginEvent.TYPE, new AuthenticationLoginEvent.Handler() {
			@Override
			public void onEvent() {
				if(mainPanel == null) {
					mainPanel = new MainPanel();
				}
				entryPointRootPanel.add(mainPanel, 0, 0);		
				authCheckTimer.scheduleRepeating(authenticationTimerMS);
			}
	    });
		
	}
}
