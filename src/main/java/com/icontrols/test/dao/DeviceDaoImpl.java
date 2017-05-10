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
		sqlSession.update(PATH + "updateDeviceState", map);
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
		sqlSession.insert(PATH + "insertDeviceGroup", map);
	}

	@Override
	public List<String> getDeviceGroupDids(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectList(PATH + "getDeviceGroupDids", map);
	}

	@Override
	public String getUIdByDId(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(PATH + "getUIdByDId", map);
	}

	@Override
	public void updateDeviceStateSubscription(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		sqlSession.update(PATH + "updateDeviceStateSubscription", map);
	}

	@Override
	public String getSubscriptionIdByDId(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(PATH+"getSubscriptionIdByDId", map);
	}

	@Override
	public Integer getSubscriptionCnt(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(PATH+"getSubscriptionCnt", map);
	}

	@Override
	public void updateGroupState(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		
		sqlSession.update(PATH+"updateGroupState", map);
		
	}

	@Override
	public List<String> getGIdBydId(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectList(PATH+"getGIdBydId", map);
	}

	@Override
	public Integer getDeviceStateByDId(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(PATH+"getDeviceStateByDId", map);
	}

	@Override
	public List<String> getUIdsByDId(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectList(PATH+"getUIdsByDId", map);
	}

	@Override
	public void deleteGroupDevice(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		sqlSession.delete(PATH+"deleteGroupDevice", map);
		
	}
}
