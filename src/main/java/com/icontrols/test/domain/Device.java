package com.icontrols.test.domain;

import java.util.Date;

public class Device {

	private String uId;
	private String dId;
	private String name;
	private Date uptDt;
	private Integer state;
	private String dtId;

	public Device(String uId, String dId, String name, String dtId) {
		super();
		this.uId = uId;
		this.dId = dId;
		this.name = name;
		this.dtId = dtId;
	}

	public Device(String uId, String dId, String name) {
		super();
		this.uId = uId;
		this.dId = dId;
		this.name = name;
	}

	public Device(String uId, String dId, String name, Integer state) {
		super();
		this.uId = uId;
		this.dId = dId;
		this.name = name;
		this.state = state;
	}
	
	public Device(String uId, String dId, String name, Integer state, String dtId) {
		super();
		this.uId = uId;
		this.dId = dId;
		this.name = name;
		this.state = state;
		this.dtId = dtId;
	}


	public Device(String dId, String name) {
		super();
		this.dId = dId;
		this.name = name;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getdId() {
		return dId;
	}

	public void setdId(String dId) {
		this.dId = dId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getUptDt() {
		return uptDt;
	}

	public void setUptDt(Date uptDt) {
		this.uptDt = uptDt;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getDtId() {
		return dtId;
	}

	public void setDtId(String dtId) {
		this.dtId = dtId;
	}

	@Override
	public String toString() {
		return "Device [uId=" + uId + ", dId=" + dId + ", name=" + name + ", uptDt=" + uptDt + ", state=" + state
				+ ", dtId=" + dtId + "]";
	}

}
