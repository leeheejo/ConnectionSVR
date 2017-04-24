package com.icontrols.test.service;

import java.util.HashMap;
import java.util.List;

import com.icontrols.test.domain.IparkAccessToken;

public interface IparkAccessTokenService {
	public void insertIparkAccessToken(IparkAccessToken iparkAccessToken);
	public String getIparkAccessTokenById (String uId);
	public List<IparkAccessToken> getAllIparkAccessToken();
	public void updateIparkAccessToken(IparkAccessToken iparkAccessToken);
}
