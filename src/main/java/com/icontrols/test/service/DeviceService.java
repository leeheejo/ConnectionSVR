package com.icontrols.test.service;

import java.util.HashMap;
import java.util.List;

import com.icontrols.test.domain.Device;
import com.icontrols.test.domain.DeviceGroup;

public interface DeviceService {
	public List<Device> getDeviceById(String uId);
	public void insertDevice(Device device);
	public void updateDeviceState(Integer state, String dId, String uId);
	public Integer getDeviceCmpCode(String dId, String uId);
	public void deleteDevice(String uId, String dId);
	public String getDIdByName(String uId, String name, int cmpCode);
	
	public void insertDeviceGroup(DeviceGroup dg);
	public String getDeviceGroupDids(String uId, String gId);
}
