package com.icontrols.test.domain;

import java.util.Date;

public class Device {

	private String uId;
	private String dId;
	private String name;
	private Date uptDt;
	private Integer state;
	private String dtId;
	private Integer cmpCode;
	private String subscriptionId;

	public Device(String uId, String dId, String name, String dtId, Integer cmpCode, String subscriptionId) {
		super();
		this.uId = uId;
		this.dId = dId;
		this.name = name;
		this.dtId = dtId;
		this.cmpCode = cmpCode;
		this.subscriptionId = subscriptionId;
	}

	public Device(String uId, String dId, String name, Integer state, String dtId, Integer cmpCode) {
		super();
		this.uId = uId;
		this.dId = dId;
		this.name = name;
		this.dtId = dtId;
		this.state = state;
		this.cmpCode = cmpCode;
	}

	public Device(String uId, String dId, String name, String dtId, int cmpCode) {
		super();
		this.uId = uId;
		this.dId = dId;
		this.name = name;
		this.dtId = dtId;
		this.cmpCode = cmpCode;
	}

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

	public int getCmpCode() {
		return cmpCode;
	}

	public void setCmpCode(int cmpCode) {
		this.cmpCode = cmpCode;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public void setCmpCode(Integer cmpCode) {
		this.cmpCode = cmpCode;
	}

	@Override
	public String toString() {
		return "Device [uId=" + uId + ", dId=" + dId + ", name=" + name + ", uptDt=" + uptDt + ", state=" + state
				+ ", dtId=" + dtId + "]";
	}

}
