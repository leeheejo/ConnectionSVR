package com.icontrols.test.dao;

import java.util.HashMap;

import com.icontrols.test.domain.PhilipsHueBridge;

public interface PhilipsHueBridgeDao {

	public void insertPhilipsHueBridge(HashMap<String, Object> map);
	public String getPhilipsHueBridgeById(HashMap<String, Object> map);
	public void updatePhilipsHueBridge (HashMap<String, Object> map);
	public String getPhilipsHueUsernameById(HashMap<String, Object> map);
	
}
