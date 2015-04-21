package com.datadynamic.shared.pojos;

import java.io.Serializable;

public class Movie implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long ID;
	private String name;
	private String genre;
	
	public Movie() {
		
	}
	
	public Movie(long ID, String name, String genre) {
		super();
		this.ID = ID;
		this.name = name;
		this.genre = genre;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public void setGenre(String genre) {
		this.genre = genre;
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}
}
