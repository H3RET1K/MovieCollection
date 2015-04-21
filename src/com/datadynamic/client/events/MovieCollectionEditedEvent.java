package com.datadynamic.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class MovieCollectionEditedEvent extends GwtEvent<MovieCollectionEditedEvent.Handler> {
	public static Type<Handler> TYPE = new Type<Handler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {		
		handler.onEvent();
	}
	
	public interface Handler extends EventHandler {
		void onEvent();
	}
}
