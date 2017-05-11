package com.icontrols.test.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.icontrols.test.domain.Device;

public interface DeviceDao {
	public List<Device> getDeviceById(HashMap<String, Object> map);
	public void insertDevice(HashMap<String, Object> map);
	public void updateDeviceState(HashMap<String, Object> map);
	public Integer getDeviceCmpCode(HashMap<String, Object> map);
	public void deleteDevice(HashMap<String, Object> map);
	public String getDIdByName (HashMap<String, Object> map);
	public String getUIdByDId (HashMap<String, Object> map);
	public List<String> getUIdsByDId (HashMap<String, Object> map);
	public Integer getDeviceStateByDId (HashMap<String, Object> map);
	
	public void updateDeviceStateSubscription(HashMap<String, Object> map);
	public String getSubscriptionIdByDId(HashMap<String, Object> map);
	public Integer getSubscriptionCnt(HashMap<String, Object> map);
	
	
	public void insertDeviceGroup(HashMap<String, Object> map);
	public List<String> getDeviceGroupDids(HashMap<String, Object> map);
	public void updateGroupState(HashMap<String, Object> map);
	public List<String> getGIdBydId (HashMap<String, Object> map);
	public void deleteGroupDevice (HashMap<String, Object> map);
	public List<Device> getGroupByUId (HashMap<String, Object> map);
	public void deleteDeviceFromGroup (HashMap<String, Object> map);
	
}
