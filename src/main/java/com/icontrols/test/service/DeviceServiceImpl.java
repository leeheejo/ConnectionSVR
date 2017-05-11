package com.icontrols.test.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icontrols.test.dao.DeviceDao;
import com.icontrols.test.domain.Device;
import com.icontrols.test.domain.DeviceGroup;

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
		map.put("dtId", device.getDtId());
		map.put("name", device.getName());
		map.put("cmpCode", device.getCmpCode());
		map.put("state", device.getState());
		map.put("subscriptionId", device.getSubscriptionId());

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
	public Integer getDeviceCmpCode(String dId, String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dId", dId);
		map.put("uId", uId);

		return deviceDao.getDeviceCmpCode(map);
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

	@Override
	public void insertDeviceGroup(DeviceGroup dg) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dIds", dg.getdIds());
		map.put("uId", dg.getuId());
		map.put("gId", dg.getgId());

		deviceDao.insertDeviceGroup(map);
	}

	@Override
	public List<String> getDeviceGroupDids(String uId, String gId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("gId", gId);
		map.put("uId", uId);

		return deviceDao.getDeviceGroupDids(map);
	}

	@Override
	public String getUIdByDId(String dId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dId", dId);
		return deviceDao.getUIdByDId(map);
	}

	@Override
	public void updateDeviceStateSubscription(Integer state, String dId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dId", dId);
		map.put("state", state);

		deviceDao.updateDeviceStateSubscription(map);

	}

	@Override
	public String getSubscriptionIdByDId(String dId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dId", dId);

		return deviceDao.getSubscriptionIdByDId(map);
	}

	@Override
	public Integer getSubscriptionCnt(String dId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dId", dId);

		return deviceDao.getSubscriptionCnt(map);
	}

	@Override
	public void updateGroupState(Integer state, String dId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dId", dId);
		map.put("state", state);

		deviceDao.updateGroupState(map);
	}

	@Override
	public List<String> getGIdBydId(String dId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dId", dId);
		return deviceDao.getGIdBydId(map);
	}

	@Override
	public Integer getDeviceStateByDId(String dId, String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dId", dId);
		map.put("uId", uId);

		return deviceDao.getDeviceStateByDId(map);
	}

	@Override
	public List<String> getUIdsByDId(String dId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dId", dId);

		return deviceDao.getUIdsByDId(map);
	}

	@Override
	public void deleteGroupDevice(String gId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("gId", gId);
		deviceDao.deleteGroupDevice(map);
	}

	@Override
	public List<Device> getGroupByUId(String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uId", uId);

		return deviceDao.getGroupByUId(map);
	}

	@Override
	public void deleteDeviceFromGroup(String dId, String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dId", dId);
		map.put("uId", uId);

		deviceDao.deleteDeviceFromGroup(map);
	}

}
