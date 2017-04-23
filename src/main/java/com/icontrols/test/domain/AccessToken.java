package com.icontrols.test.domain;

public class AccessToken {
	private String uId;
	private String access_token;
	private String token_type;
	private String expires_in;
	private String refresh_token;
	private String issuedTime;

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getIssuedTime() {
		return issuedTime;
	}

	public void setIssuedTime(String issuedTime) {
		this.issuedTime = issuedTime;
	}

	@Override
	public String toString() {
		return "AccessToken [uId=" + uId + ", access_token=" + access_token + ", token_type="
				+ token_type + ", expires_in=" + expires_in + ", refresh_token=" + refresh_token + "]";
	}

}
