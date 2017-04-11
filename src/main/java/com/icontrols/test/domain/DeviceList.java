package com.icontrols.test.domain;

public class DeviceList {
	
	private String uId;
	private String cmpCode;
	private String id;
	private String dtid;
	private String name;
	private int manifestVersion;
	private String manifestVersionPolicy;
	private boolean needProviderAuth;
	private String cloudAuthorization;
	private String createdOn;
	private boolean connected;
	private boolean sharedWithOthers;
	private String sharedWithMe;
	
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	public String getCmpCode() {
		return cmpCode;
	}
	public void setCmpCode(String cmpCode) {
		this.cmpCode = cmpCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDtid() {
		return dtid;
	}
	public void setDtid(String dtid) {
		this.dtid = dtid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getManifestVersion() {
		return manifestVersion;
	}
	public void setManifestVersion(int manifestVersion) {
		this.manifestVersion = manifestVersion;
	}
	public String getManifestVersionPolicy() {
		return manifestVersionPolicy;
	}
	public void setManifestVersionPolicy(String manifestVersionPolicy) {
		this.manifestVersionPolicy = manifestVersionPolicy;
	}
	public boolean isNeedProviderAuth() {
		return needProviderAuth;
	}
	public void setNeedProviderAuth(boolean needProviderAuth) {
		this.needProviderAuth = needProviderAuth;
	}
	public String getCloudAuthorization() {
		return cloudAuthorization;
	}
	public void setCloudAuthorization(String cloudAuthorization) {
		this.cloudAuthorization = cloudAuthorization;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public boolean isConnected() {
		return connected;
	}
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	public boolean isSharedWithOthers() {
		return sharedWithOthers;
	}
	public void setSharedWithOthers(boolean shaedWithOthers) {
		this.sharedWithOthers = shaedWithOthers;
	}
	public String getSharedWithMe() {
		return sharedWithMe;
	}
	public void setSharedWithMe(String sharedWithMe) {
		this.sharedWithMe = sharedWithMe;
	}
	@Override
	public String toString() {
		return "DeviceList [uId=" + uId + ", cmpCode=" + cmpCode + ", id=" + id + ", dtid=" + dtid + ", name=" + name
				+ ", manifestVersion=" + manifestVersion + ", manifestVersionPolicy=" + manifestVersionPolicy
				+ ", needProviderAuth=" + needProviderAuth + ", cloudAuthorization=" + cloudAuthorization
				+ ", createdOn=" + createdOn + ", connected=" + connected + ", sharedWithOthers=" + sharedWithOthers
				+ ", sharedWithMe=" + sharedWithMe + "]";
	}
	
	
	
}
