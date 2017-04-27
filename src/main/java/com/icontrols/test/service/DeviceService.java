package com.icontrols.test.service;

import java.util.HashMap;
import java.util.List;

import com.icontrols.test.domain.Device;

public interface DeviceService {
	public List<Device> getDeviceById(String uId);
	public void insertDevice(Device device);
	public void updateDeviceState(Integer state, String dId, String uId);
	public String getDeviceTypeId(String dId, String uId);
	public void deleteDevice(String uId, String dId);
	public String getDIdByName(String uId, String name, int cmpCode);
}
