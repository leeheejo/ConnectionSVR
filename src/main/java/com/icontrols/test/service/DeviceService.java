package com.icontrols.test.service;

import java.util.HashMap;
import java.util.List;

import com.icontrols.test.domain.Device;

public interface DeviceService {
	public List<Device> getDeviceById(String uId);
	public void insertDevice(Device device);
	public void updateDeviceState(Integer state, String dId);
	public String getDeviceTypeId(String dId, String uId);
}
