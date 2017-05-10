package com.icontrols.test.service;

import java.util.HashMap;
import java.util.List;

import com.icontrols.test.domain.Device;
import com.icontrols.test.domain.DeviceGroup;

public interface DeviceService {
	public List<Device> getDeviceById(String uId);
	public void insertDevice(Device device);
	public void updateDeviceState(Integer state, String dId, String uId);
	public void updateDeviceStateSubscription(Integer state, String dId);
	public Integer getDeviceCmpCode(String dId, String uId);
	public void deleteDevice(String uId, String dId);
	public String getDIdByName(String uId, String name, int cmpCode);
	public String getUIdByDId (String dId);
	public List<String> getUIdsByDId (String dId);
	public String getSubscriptionIdByDId(String dId);
	public Integer getSubscriptionCnt(String dId);
	
	public void insertDeviceGroup(DeviceGroup dg);
	public List<String> getDeviceGroupDids(String uId, String gId);
	public void updateGroupState (Integer state, String dId);
	public List<String> getGIdBydId (String dId);
	public Integer getDeviceStateByDId (String dId, String uId);
	public void deleteGroupDevice (String gId);
}
