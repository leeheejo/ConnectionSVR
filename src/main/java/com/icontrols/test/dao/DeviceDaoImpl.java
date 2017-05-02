package com.icontrols.test.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.icontrols.test.domain.Device;

@Repository
public class DeviceDaoImpl implements DeviceDao {

	@Autowired
	private SqlSessionTemplate sqlSession;

	// mapper's name
	private final String PATH = "Device.";

	@Override
	public List<Device> getDeviceById(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectList(PATH + "getDeviceById", map);
	}

	@Override
	public void insertDevice(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		sqlSession.insert(PATH + "insertDevice", map);
	}

	@Override
	public void updateDeviceState(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		sqlSession.insert(PATH + "updateDeviceState", map);
	}

	@Override
	public Integer getDeviceCmpCode(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(PATH + "getDeviceCmpCode", map);
	}

	@Override
	public void deleteDevice(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		sqlSession.delete(PATH + "deleteDevice", map);

	}

	@Override
	public String getDIdByName(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(PATH + "getDIdByName", map);
	}

	@Override
	public void insertDeviceGroup(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		sqlSession.insert(PATH+"insertDeviceGroup", map);
	}

	@Override
	public String getDeviceGroupDids(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(PATH+"getDeviceGroupDids", map);
	}
}
