package com.icontrols.test.dao;

import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.icontrols.test.domain.PhilipsHueBridge;

@Repository
public class PhilipsHueBridgeDaoImpl implements PhilipsHueBridgeDao {
	@Autowired
	private SqlSessionTemplate sqlSession;

	// mapper's name
	private final String PATH = "PhilipsHueBridge.";

	@Override
	public void insertPhilipsHueBridge(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		sqlSession.insert(PATH+"insertPhilipsHueBridge", map);
	}

	@Override
	public String getPhilipsHueBridgeById(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(PATH+"getPhilipsHueBridgeById", map);
	}

	@Override
	public void updatePhilipsHueBridge(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		sqlSession.update(PATH+"updatePhilipsHueBridge", map);
	}

	@Override
	public String getPhilipsHueUsernameById(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(PATH+"getPhilipsHueUsernameById", map);
	}

}
