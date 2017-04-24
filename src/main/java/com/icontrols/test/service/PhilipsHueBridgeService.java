package com.icontrols.test.service;

import java.util.HashMap;

import com.icontrols.test.domain.PhilipsHueBridge;

public interface PhilipsHueBridgeService{

	public void insertPhilipsHueBridge(String philipsHueBridgeIp, String uId);
	public String getPhilipsHueBridgeById(String uId);
	public void updatePhilipsHueBridge(String philipsHueBridgeIp, String uId);
}
