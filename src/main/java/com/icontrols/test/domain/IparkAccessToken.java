package com.icontrols.test.domain;

public class IparkAccessToken {
	public String accessToken; 
	public String expiresIn;
	public String issuedTime;
	public String uId;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getIssuedTime() {
		return issuedTime;
	}
	public void setIssuedTime(String issuedTime) {
		this.issuedTime = issuedTime;
	}
	
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	@Override
	public String toString() {
		return "IparkAccessToken [accessToken=" + accessToken + ", expiresIn=" + expiresIn + ", issuedTime="
				+ issuedTime + "]";
	}
	
	

}
