package com.icontrols.test.dao;

import java.util.HashMap;

import org.json.JSONArray;
import org.springframework.stereotype.Repository;

@Repository(value="DeviceList")
public interface DeviceListDao {
	public void inserDeviceList(HashMap<String, Object> map);
}
