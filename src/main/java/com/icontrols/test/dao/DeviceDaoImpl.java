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
	public String getDeviceTypeId(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(PATH + "getDeviceTypeId", map);
	}
}
