package com.icontrols.test.domain;

import java.util.Date;

public class SendTestLog {

	private int num;
	private String uId;
	private int cmpCode;
	private String ddid;
	private String data;
	private String response;
	private Date sendDt;

	
	public SendTestLog(String uId, int cmpCode, String ddid, String data, String response, Date sendDt) {
		super();
		this.uId = uId;
		this.cmpCode = cmpCode;
		this.ddid = ddid;
		this.data = data;
		this.response = response;
		this.sendDt = sendDt;
	}


	public int getNum() {
		return num;
	}


	public void setNum(int num) {
		this.num = num;
	}


	public String getuId() {
		return uId;
	}


	public void setuId(String uId) {
		this.uId = uId;
	}


	public int getCmpCode() {
		return cmpCode;
	}


	public void setCmpCode(int cmpCode) {
		this.cmpCode = cmpCode;
	}


	public String getDdid() {
		return ddid;
	}


	public void setDdid(String ddid) {
		this.ddid = ddid;
	}


	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}


	public String getResponse() {
		return response;
	}


	public void setResponse(String response) {
		this.response = response;
	}


	public Date getSendDt() {
		return sendDt;
	}


	public void setSendDt(Date sendDt) {
		this.sendDt = sendDt;
	}


	@Override
	public String toString() {
		return "SendTestLog [num=" + num + ", uId=" + uId + ", cmpCode=" + cmpCode + ", ddid=" + ddid + ", data=" + data
				+ ", response=" + response + ", sendDt=" + sendDt + "]";
	}

}
