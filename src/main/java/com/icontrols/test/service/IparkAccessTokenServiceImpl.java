package com.icontrols.test.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icontrols.test.dao.IparkAccessTokenDao;
import com.icontrols.test.domain.IparkAccessToken;

@Service
public class IparkAccessTokenServiceImpl implements IparkAccessTokenService{

	@Autowired
	IparkAccessTokenDao iparkAccessTokenDao;
	
	@Override
	public void insertIparkAccessToken(IparkAccessToken iparkAccessToken) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uId", iparkAccessToken.getuId());
		map.put("accessToken", iparkAccessToken.getAccessToken());
		map.put("expiresIn", iparkAccessToken.getExpiresIn());
		map.put("issuedTime", System.currentTimeMillis());
		
		iparkAccessTokenDao.insertIparkAccessToken(map);
		
	}

	@Override
	public String getIparkAccessTokenById(String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uId", uId);
		
		return iparkAccessTokenDao.getIparkAccessTokenById(map);
	}

	@Override
	public List<IparkAccessToken> getAllIparkAccessToken() {
		// TODO Auto-generated method stub
		return iparkAccessTokenDao.getAllIparkAccessToken();
	}

	@Override
	public void updateIparkAccessToken(IparkAccessToken iparkAccessToken) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uId", iparkAccessToken.getuId());
		map.put("accessToken", iparkAccessToken.getAccessToken());
		map.put("expiresIn", iparkAccessToken.getExpiresIn());
		map.put("issuedTime", System.currentTimeMillis());
		
		iparkAccessTokenDao.updateIparkAccessToken(map);
	}

}
