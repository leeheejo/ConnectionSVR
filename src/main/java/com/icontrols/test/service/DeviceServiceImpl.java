package com.icontrols.test.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icontrols.test.dao.DeviceDao;
import com.icontrols.test.domain.Device;

@Service
public class DeviceServiceImpl implements DeviceService {

	@Autowired
	DeviceDao deviceDao;

	@Override
	public List<Device> getDeviceById(String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uId", uId);

		return deviceDao.getDeviceById(map);
	}

	@Override
	public void insertDevice(Device device) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uId", device.getuId());
		map.put("dId", device.getdId());
		map.put("name", device.getName());
		map.put("cmpCode", device.getCmpCode());

		deviceDao.insertDevice(map);
	}

	@Override
	public void updateDeviceState(Integer state, String dId, String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("state", state);
		map.put("dId", dId);
		map.put("uId", uId);

		deviceDao.updateDeviceState(map);
	}

	@Override
	public String getDeviceTypeId(String dId, String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dId", dId);
		map.put("uId", uId);
		
		return deviceDao.getDeviceTypeId(map);
	}

	@Override
	public void deleteDevice(String uId, String dId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dId", dId);
		map.put("uId", uId);
		
		deviceDao.deleteDevice(map);
		
	}

	@Override
	public String getDIdByName(String uId, String name, int cmpCode) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("uId", uId);
		map.put("cmpCode", cmpCode);
		
		return deviceDao.getDIdByName(map);
	}


}
