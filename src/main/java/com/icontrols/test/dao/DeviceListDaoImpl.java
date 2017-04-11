package com.icontrols.test.dao;

import java.util.HashMap;

import org.json.JSONArray;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceListDaoImpl implements DeviceListDao{

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private final String PATH = "DeviceList."; //DeviceList.xml <- mapper name space
	
	@Override
	public void inserDeviceList(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		sqlSession.insert(PATH + "inserDeviceList", map);
	}

}
