package com.icontrols.test.dao;

import java.util.HashMap;
import java.util.List;

import com.icontrols.test.domain.IparkAccessToken;

public interface IparkAccessTokenDao {
	
	public void insertIparkAccessToken(HashMap<String, Object> map);
	public String getIparkAccessTokenById (HashMap<String, Object> map);
	public List<IparkAccessToken> getAllIparkAccessToken();
	public void updateIparkAccessToken(HashMap<String, Object> map);
	
}
