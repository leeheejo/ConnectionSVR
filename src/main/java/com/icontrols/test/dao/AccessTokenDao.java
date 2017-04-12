package com.icontrols.test.dao;

import java.util.HashMap;

import org.springframework.stereotype.Repository;


public interface AccessTokenDao {
	
	public void insertAccessToken(HashMap<String, Object> map);
	public int tokenCheck(HashMap<String, Object> map);
	public void updateAccessToken(HashMap<String, Object> map);
	public String getAccessTokenById(HashMap<String, Object> map);
	
}
