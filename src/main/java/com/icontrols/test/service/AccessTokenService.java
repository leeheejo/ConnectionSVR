package com.icontrols.test.service;
import java.util.HashMap;
import java.util.List;

import com.icontrols.test.domain.AccessToken;

public interface AccessTokenService {

	public void insertAccessToken(AccessToken accessToken);
	public int tokenCheck(String uId);
	public void updateAccessToken(AccessToken accessToken);
	public String getAccessTokenById(String uId);
	public List<AccessToken> getAllAccessToken();
}
