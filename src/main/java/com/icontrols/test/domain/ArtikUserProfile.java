package com.icontrols.test.domain;

public class ArtikUserProfile {
	
	public String uId;
	public String id;
	public String name;
	public String email;
	public String fullName;
	public String saIdentity;
	public String accountType;
	public String createdOn;
	public String modifiedOn;
	
	
	public ArtikUserProfile(String uId, String id, String name, String email, String fullName, String saIdentity,
			String accountType, String createdOn, String modifiedOn) {
		super();
		this.uId = uId;
		this.id = id;
		this.name = name;
		this.email = email;
		this.fullName = fullName;
		this.saIdentity = saIdentity;
		this.accountType = accountType;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
	}
	
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getSaIdentity() {
		return saIdentity;
	}
	public void setSaIdentity(String saIdentity) {
		this.saIdentity = saIdentity;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	@Override
	public String toString() {
		return "ArtikUserProfile [uId=" + uId + ", id=" + id + ", name=" + name + ", email=" + email + ", fullName="
				+ fullName + ", saIdentity=" + saIdentity + ", accountType=" + accountType + ", createdOn=" + createdOn
				+ ", modifiedOn=" + modifiedOn + "]";
	}
	
}
