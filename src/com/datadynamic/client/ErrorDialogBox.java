package com.datadynamic.client;

import com.datadynamic.shared.Labels;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class ErrorDialogBox extends DialogBox {
	private Label lblError;

	public ErrorDialogBox() {
		AbsolutePanel absolutePanel = new AbsolutePanel();		
		absolutePanel.setSize("293px", "169px");
		this.setWidget(absolutePanel);
		
		lblError = new Label("");
		absolutePanel.add(lblError, 10, 10);
		lblError.setSize("273px", "114px");
		
		Button btnClose = new Button("Close");
		btnClose.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ErrorDialogBox.this.removeFromParent();
			}
		});
		absolutePanel.add(btnClose, 126, 130);
		this.show();
		this.center();
		this.setModal(true);		
	}
	
	public void setError(String title, String errorMsg) {
		this.setTitle(title);
		this.lblError.setText(errorMsg);
	}
	
	public void setNetworkError() {
		this.setTitle(Labels.SERVER_ERROR_TITLE);
		this.lblError.setText(Labels.SERVER_ERROR_MSG);
	}
}
