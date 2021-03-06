package com.datadynamic.shared.pojos;

import java.io.Serializable;

public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private long ID;
	private String username;
	private int invalidattempts;
	private String role;
	private int islocked;
	
	public User() {
		
	}
	
	public User(long ID, String username, int invalidattempts, String role, int islocked) {
		super();
		this.ID = ID;
		this.username = username;
		this.invalidattempts = invalidattempts;
		this.role = role;
		this.islocked = islocked;
	}
	
	public String getName() {
		return username;
	}

	public void setUserName(String username) {
		this.username = username;
	}
	
	public int getInvalidAttempts() {
		return invalidattempts;
	}
	
	public void setInvalidAttempts(int InvalidAttempts) {
		invalidattempts = InvalidAttempts;
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}
	
	public String getRole(){
		return role;
	}
	
	public void setRole(String role){
		this.role = role;
	}
	
	public int getIsLocked() {
		return islocked;
	}
	
	public void setIsLocked(int IsLocked) {
		islocked = IsLocked;
	}
}
