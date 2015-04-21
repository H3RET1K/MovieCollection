package com.datadynamic.client.remoteservices;

import com.google.gwt.core.client.GWT;

public class Services {
	public static MovieServiceAsync movieService = GWT.create(MovieService.class);
}
