package com.icontrols.test.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icontrols.test.dao.AccessTokenDao;
import com.icontrols.test.domain.AccessToken;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

	@Autowired
	private AccessTokenDao accessTokenDao;

	@Override
	public void insertAccessToken(AccessToken accessToken) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uId", accessToken.getuId());
		map.put("accessToken", accessToken.getAccess_token());
		map.put("tokenType", accessToken.getToken_type());
		map.put("expiresIn", accessToken.getExpires_in());
		map.put("refreshToken", accessToken.getRefresh_token());

		accessTokenDao.insertAccessToken(map);

	}

	@Override
	public int tokenCheck(String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uId", uId);

		return accessTokenDao.tokenCheck(map);
	}

	@Override
	public void updateAccessToken(AccessToken accessToken) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("uId", accessToken.getuId());
		map.put("accessToken", accessToken.getAccess_token());
		map.put("tokenType", accessToken.getToken_type());
		map.put("expiresIn", accessToken.getExpires_in());
		map.put("refreshToken", accessToken.getRefresh_token());

		accessTokenDao.updateAccessToken(map);
	}

	@Override
	public String getAccessTokenById(String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("uId", uId);

		return accessTokenDao.getAccessTokenById(map);
	}

}
