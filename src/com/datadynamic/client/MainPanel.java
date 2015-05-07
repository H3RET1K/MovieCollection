package com.datadynamic.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.cellview.client.DataGrid;
import com.datadynamic.client.events.Events;
import com.datadynamic.client.events.MovieCollectionEditedEvent;
import com.datadynamic.client.remoteservices.Services;

import com.datadynamic.shared.Labels;
import com.datadynamic.shared.pojos.*;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Composite;

public class MainPanel extends Composite {
	private DataGrid<Movie> dataGrid;	
	private long selectedMovieID = -1;	
		
	public MainPanel() {
		AbsolutePanel absolutePanel = new AbsolutePanel();		
		absolutePanel.setSize("450px", "300px");
		
		dataGrid = new DataGrid<Movie>();
		absolutePanel.add(dataGrid, 10, 66);
		dataGrid.setSize("427px", "224px");
		
		TextColumn<Movie> nameColumn = new TextColumn<Movie>() {
			@Override
			public String getValue(Movie object) {
				return object.getName().toString();
			}
		};
		dataGrid.addColumn(nameColumn, "Name");
		
		TextColumn<Movie> genreColumn = new TextColumn<Movie>() {
			@Override
			public String getValue(Movie object) {
				return object.getGenre().toString();
			}
		};
		dataGrid.addColumn(genreColumn, "Genre");
		
		Button btnAddMovie = new Button("Add");
		btnAddMovie.setText("Add");
		btnAddMovie.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				@SuppressWarnings("unused")
				AddMovie addMovie = new AddMovie();
			}
		});
		absolutePanel.add(btnAddMovie, 398, 10);
		
		HTML htmlMovieCollectionTitle = new HTML("<h4>Movie Collection</h4>", true);
		absolutePanel.add(htmlMovieCollectionTitle, 10, 10);
		htmlMovieCollectionTitle.setSize("343px", "52px");
		
		Button btnRemove = new Button("Remove");
		btnRemove.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Services.movieService.removeMovie(MainPanel.this.selectedMovieID, new AsyncCallback<ActionResponse> () {
					@Override
					public void onFailure(Throwable caught) {
						ErrorDialogBox errBox = new ErrorDialogBox();
						errBox.setNetworkError();
					}

					@Override
					public void onSuccess(ActionResponse result) {		
						if(result.getSuccess() == false) {
							ErrorDialogBox errBox = new ErrorDialogBox();
							errBox.setError(Labels.REMOVE_MOVIE_ERROR_TITLE, Labels.REMOVE_MOVIE_ERROR_TITLE + Labels.LABEL_CONCAT + result.getReason());
						} else {						
							Events.EVENT_BUS.fireEvent(new MovieCollectionEditedEvent());
						}
					}
				});
			}
		});
		absolutePanel.add(btnRemove, 328, 10);			
		
		/*
		 Code
		*/
		
		// Movie Grid Selection
		final SingleSelectionModel<Movie> selectionModel = new SingleSelectionModel<Movie>();
		dataGrid.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				Movie selected = selectionModel.getSelectedObject();
				if (selected != null) {
					selectedMovieID = selected.getID();
				}
			}
		});
		
		/*
		 Events
		*/
		
		Events.EVENT_BUS.addHandler(MovieCollectionEditedEvent.TYPE, new MovieCollectionEditedEvent.Handler() {
			@Override
			public void onEvent() {
				loadMovieData();
			}
	    });
		
		this.initWidget(absolutePanel);
		
		loadMovieData();
	}
	
	public void loadMovieData() {
		Services.movieService.getMovieCollection(new AsyncCallback<ArrayList<Movie>> () {
			@Override
			public void onFailure(Throwable caught) {
				ErrorDialogBox errBox = new ErrorDialogBox();
				errBox.setNetworkError();
			}

			@Override
			public void onSuccess(ArrayList<Movie> result) {				
				dataGrid.setRowCount(result.size(), true);
				dataGrid.setRowData(0, result);				
			}
		});	
	}	
}
