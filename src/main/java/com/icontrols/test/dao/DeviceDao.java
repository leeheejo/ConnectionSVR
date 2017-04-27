package com.icontrols.test.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.icontrols.test.domain.Device;

public interface DeviceDao {
	public List<Device> getDeviceById(HashMap<String, Object> map);
	public void insertDevice(HashMap<String, Object> map);
	public void updateDeviceState(HashMap<String, Object> map);
	public String getDeviceTypeId(HashMap<String, Object> map);
	public void deleteDevice(HashMap<String, Object> map);
	public String getDIdByName (HashMap<String, Object> map);
	
}
