package com.datadynamic.shared.pojos;

import java.io.Serializable;

public class ActionResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private Boolean success;
	private String reason;
	public ActionResponse() {
		this.success = true;
		this.reason = null;
	}
	public ActionResponse(Boolean success, String reason) {
		this.success = success;
		this.reason = reason;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
}
