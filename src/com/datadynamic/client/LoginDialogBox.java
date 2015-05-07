package com.datadynamic.client;

import com.datadynamic.client.events.AuthenticationLoginEvent;
import com.datadynamic.client.events.Events;
import com.datadynamic.client.remoteservices.Services;
import com.datadynamic.shared.pojos.ActionResponse;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class LoginDialogBox extends DialogBox {
	private TextBox textBoxUsername;
	private TextBox textBoxSecurityToken;

	public LoginDialogBox() {
		AbsolutePanel absolutePanel = new AbsolutePanel();		
		absolutePanel.setSize("300px", "160px");
		
		
		Button btnLogin = new Button("New button");
		btnLogin.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int tokenParsed = 0;
				if(textBoxSecurityToken.getValue() != "") {
					tokenParsed = Integer.parseInt(textBoxSecurityToken.getValue());
				}
				Services.authenticationService.login(
					textBoxUsername.getValue(),
					tokenParsed,
					(new AsyncCallback<ActionResponse> () {
						@Override
						public void onFailure(Throwable caught) {
							ErrorDialogBox errBox = new ErrorDialogBox();
							errBox.setNetworkError();
						}

						@Override
						public void onSuccess(ActionResponse result) {
							if(result.getSuccess() == false) {
								ErrorDialogBox errBox = new ErrorDialogBox();
								errBox.setError("Login Error", result.getReason());
							} else {
								LoginDialogBox.this.removeFromParent();
								Events.EVENT_BUS.fireEvent(new AuthenticationLoginEvent());
							}
						}
					})
				);
			}
		});
		btnLogin.setText("Login");
		absolutePanel.add(btnLogin, 239, 127);
		
		textBoxUsername = new TextBox();
		absolutePanel.add(textBoxUsername, 10, 31);
		textBoxUsername.setSize("266px", "18px");
		
		textBoxSecurityToken = new TextBox();
		absolutePanel.add(textBoxSecurityToken, 10, 91);
		textBoxSecurityToken.setSize("266px", "18px");
		
		Label lblUserName = new Label("User Name:");
		absolutePanel.add(lblUserName, 10, 7);
		
		Label lblSecuirtyToken = new Label("Secuirty Token:");
		absolutePanel.add(lblSecuirtyToken, 10, 67);
		
		this.setWidget(absolutePanel);
		this.setText("User Login");
		this.show();
		this.center();
		this.setModal(true);
	}
}
