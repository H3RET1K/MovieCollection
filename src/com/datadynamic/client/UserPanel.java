package com.datadynamic.client;

import java.util.ArrayList;

import com.datadynamic.client.events.Events;
import com.datadynamic.client.events.UserGroupEditedEvent;
import com.datadynamic.client.remoteservices.Services;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.datadynamic.shared.Labels;
import com.datadynamic.shared.pojos.*;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.user.client.ui.PopupPanel.AnimationType;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;


public class UserPanel extends DialogBox {
	private DataGrid<User> dataGrid;	
	private long selectedUserID = -1;
	private TextBox textBoxUsername;
	private TextBox textBoxRole;
	
	public UserPanel() {
		setSize("480px", "358px");
		AbsolutePanel absolutePanel = new AbsolutePanel();		
		absolutePanel.setSize("454px", "312px");
				
		dataGrid = new DataGrid<User>();
		absolutePanel.add(dataGrid, 10, 74);
		dataGrid.setSize("383px", "228px");
		
		TextColumn<User> nameColumn = new TextColumn<User>() {
			@Override
			public String getValue(User object){
				return object.getName().toString();
			}
		};
		dataGrid.addColumn(nameColumn, "Users");
		
		TextColumn<User> attemptsColumn = new TextColumn<User>() {
			@Override
			public String getValue(User object){
				return Integer.toString(object.getInvalidAttempts());
			}
		};
		dataGrid.addColumn(attemptsColumn, "Attempts");		 
		
		TextColumn<User> roleColumn = new TextColumn<User>() {
			@Override
			public String getValue(User object) {
				return object.getRole().toString();
			}
		};
		dataGrid.addColumn(roleColumn, "Role");		
		
		
		ButtonCell deleteButton = new ButtonCell();
		Column <User, String> delete = new Column <User, String>(deleteButton) {
			@Override
			public String getValue(User user) {
				return "Delete";
			}
		};			
		dataGrid.addColumn(delete, "Delete");
		
		delete.setFieldUpdater(new FieldUpdater<User, String>() {
			@Override
			public void update (int index, User object, String value) { 
				//Window.alert("You clicked: " + object.getID());
				Services.userService.removeUser(object.getID(), new AsyncCallback<ActionResponse> () {
					@Override
					public void onFailure(Throwable caught) {
						ErrorDialogBox errBox = new ErrorDialogBox();
						errBox.setNetworkError();						
					}

					@Override
					public void onSuccess(ActionResponse result) {
						if(result.getSuccess() == false) {
							ErrorDialogBox errBox = new ErrorDialogBox();
							errBox.setError(Labels.ADD_USER_ERROR_TITLE, Labels.ADD_USER_ERROR_MSG + Labels.LABEL_CONCAT + result.getReason());
						} else {
							Events.EVENT_BUS.fireEvent(new UserGroupEditedEvent());
						}
					} 
				});	
			}
		});		
		
		Button btnAdd = new Button("New button");
		btnAdd.setText("Add User");		
		btnAdd.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {					
				Services.userService.addUser(textBoxUsername.getText(), textBoxRole.getText(), new AsyncCallback<ActionResponse> () {
					@Override
					public void onFailure(Throwable caught) {
						ErrorDialogBox errBox = new ErrorDialogBox();
						errBox.setNetworkError();						
					}

					@Override
					public void onSuccess(ActionResponse result) {
						if(result.getSuccess() == false) {
							ErrorDialogBox errBox = new ErrorDialogBox();
							errBox.setError(Labels.ADD_USER_ERROR_TITLE, Labels.ADD_USER_ERROR_MSG + Labels.LABEL_CONCAT + result.getReason());
						} else {
							Events.EVENT_BUS.fireEvent(new UserGroupEditedEvent());	
							textBoxUsername.setText("");
							textBoxRole.setText("");
						}
					} 
				});				
			}
		});
		absolutePanel.add(btnAdd, 259, 24);
		
		
		Button btnBack = new Button("Back");
		btnBack.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				UserPanel.this.removeFromParent();
			}
		});
		absolutePanel.add(btnBack, 407, 0);		
		
		
		final SingleSelectionModel<User> selectionModel = new SingleSelectionModel<User>();
		dataGrid.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				User selected = selectionModel.getSelectedObject();
				if (selected != null) {
					selectedUserID = selected.getID();
				}
			}
		});
		
		Events.EVENT_BUS.addHandler(UserGroupEditedEvent.TYPE, new UserGroupEditedEvent.Handler() {
			@Override
			public void onEvent() {
				loadUserData();
			}
	    });
		
		
		this.setWidget(absolutePanel);		
		
		textBoxUsername = new TextBox();
		absolutePanel.add(textBoxUsername, 10, 24);
		textBoxUsername.setSize("120px", "18px");
		
		Label lblNewLabel = new Label("Username:");
		absolutePanel.add(lblNewLabel, 10, 10);
		
		textBoxRole = new TextBox();
		absolutePanel.add(textBoxRole, 160, 24);
		textBoxRole.setSize("73px", "18px");
		
		Label lblRole = new Label("Role:");
		absolutePanel.add(lblRole, 162, 10);
		
		
		this.setText("User Management:");
		this.show();
		this.center();
		loadUserData();	
	
	}
		
	public void loadUserData() {
		Services.userService.getUsers(new AsyncCallback<ArrayList<User>> () {
			@Override
			public void onFailure(Throwable caught) {
				ErrorDialogBox errBox = new ErrorDialogBox();
				errBox.setNetworkError();
			}

			@Override
			public void onSuccess(ArrayList<User> result) {				
				dataGrid.setRowCount(result.size(), true);
				dataGrid.setRowData(0, result);				
			}
		});	
	}		
}
