package com.datadynamic.client;

import com.datadynamic.client.events.Events;
import com.datadynamic.client.events.MovieCollectionEditedEvent;
import com.datadynamic.client.remoteservices.Services;
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

public class AddMovie extends DialogBox {
	private TextBox textBoxName;	
	private TextBox textBoxGenre;
	public AddMovie() {
		AbsolutePanel absolutePanel = new AbsolutePanel();		
		absolutePanel.setSize("293px", "169px");
		
		Button btnOk = new Button("OK");
		btnOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {					
				Services.movieService.addMovie(textBoxName.getText(), textBoxGenre.getText(), new AsyncCallback<ActionResponse> () {
					@Override
					public void onFailure(Throwable caught) {
						ErrorDialogBox errBox = new ErrorDialogBox();
						errBox.setNetworkError();						
					}

					@Override
					public void onSuccess(ActionResponse result) {
						if(result.getSuccess() == false) {
							ErrorDialogBox errBox = new ErrorDialogBox();
							errBox.setError(Labels.ADD_MOVIE_ERROR_TITLE, Labels.ADD_MOVIE_ERROR_MSG + Labels.LABEL_CONCAT + result.getReason());
						} else {
							Events.EVENT_BUS.fireEvent(new MovieCollectionEditedEvent());
						}
					} 
				});
				AddMovie.this.removeFromParent();
			}
		});
		absolutePanel.add(btnOk, 248, 130);
		
		Button btnCancel = new Button("Cancel");
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				AddMovie.this.removeFromParent();
			}
		});
		absolutePanel.add(btnCancel, 186, 130);
		
		Label lblNewLabel = new Label("Name:");
		absolutePanel.add(lblNewLabel, 10, 10);
		lblNewLabel.setSize("62px", "18px");
		
		textBoxName = new TextBox();
		absolutePanel.add(textBoxName, 10, 34);
		textBoxName.setSize("263px", "18px");
		
		textBoxGenre = new TextBox();
		absolutePanel.add(textBoxGenre, 10, 94);
		textBoxGenre.setSize("263px", "18px");
		
		Label lblNewLabel_1 = new Label("Genre:");
		absolutePanel.add(lblNewLabel_1, 10, 70);
		lblNewLabel_1.setSize("62px", "18px");
		
		this.setWidget(absolutePanel);
		this.setText("Add Movie");
		this.show();
		this.center();
	}
}
