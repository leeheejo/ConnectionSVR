package com.icontrols.test.domain;

public class DeviceGroup {
	
	String uId;
	String dIds;
	String gId;
	
	public DeviceGroup(String uId, String dIds, String gId) {
		super();
		this.uId = uId;
		this.dIds = dIds;
		this.gId = gId;
	}
	
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	public String getdIds() {
		return dIds;
	}
	public void setdIds(String dIds) {
		this.dIds = dIds;
	}
	public String getgId() {
		return gId;
	}
	public void setgId(String gId) {
		this.gId = gId;
	}
	@Override
	public String toString() {
		return "DeviceGroup [uId=" + uId + ", dIds=" + dIds + ", gId=" + gId + "]";
	}

}
