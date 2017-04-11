package com.icontrols.test.domain;

public class User {
	
	private String uId;
	private String uPwd;
	private String uPhone;
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	public String getuPwd() {
		return uPwd;
	}
	public void setuPwd(String uPwd) {
		this.uPwd = uPwd;
	}
	public String getuPhone() {
		return uPhone;
	}
	public void setuPhone(String uPhone) {
		this.uPhone = uPhone;
	}
	@Override
	public String toString() {
		return "User [uId=" + uId + ", uPwd=" + uPwd + ", uPhone=" + uPhone + ", getuId()=" + getuId() + ", getuPwd()="
				+ getuPwd() + ", getuPhone()=" + getuPhone() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	

}
