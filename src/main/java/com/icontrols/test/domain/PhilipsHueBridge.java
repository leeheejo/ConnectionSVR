package com.icontrols.test.domain;

public class PhilipsHueBridge {
	public String bridgeIp;
	public String uId;
	public String username;
	
	public String getBridgeIp() {
		return bridgeIp;
	}
	public void setBridgeIp(String bridgeIp) {
		this.bridgeIp = bridgeIp;
	}
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
		return "PhilipsHueBridge [bridgeIp=" + bridgeIp + ", uId=" + uId + "]";
	}
	
}
