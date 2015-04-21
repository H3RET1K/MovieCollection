package com.datadynamic.shared;

public class FieldVerifier {
	public static boolean isValidName(String name) {
		if (name == null) {
			return false;
		}
		return name.length() > 1;
	}
	public static boolean isValidGenre(String genre) {
		if (genre == null) {
			return false;
		}
		return genre.length() > 1;
	}	
}
