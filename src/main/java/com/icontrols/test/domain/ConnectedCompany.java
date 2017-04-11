package com.icontrols.test.domain;

public class ConnectedCompany {
	
	private int cmpCode;
	private String cmpName;
	public int getCmpCode() {
		return cmpCode;
	}
	public void setCmpCode(int cmpCode) {
		this.cmpCode = cmpCode;
	}
	public String getCmpName() {
		return cmpName;
	}
	public void setCmpName(String cmpName) {
		this.cmpName = cmpName;
	}
	@Override
	public String toString() {
		return "ConnectedCompany [cmpCode=" + cmpCode + ", cmpName=" + cmpName + "]";
	}
	
	

}
