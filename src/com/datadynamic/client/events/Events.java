package com.datadynamic.client.events;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;

public class Events {
	public static SimpleEventBus EVENT_BUS = GWT.create(SimpleEventBus.class);
}
