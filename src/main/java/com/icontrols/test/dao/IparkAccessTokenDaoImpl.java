package com.icontrols.test.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.icontrols.test.domain.IparkAccessToken;

@Repository
public class IparkAccessTokenDaoImpl implements IparkAccessTokenDao {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	private final String PATH = "IparkAccessToken."; // DeviceList.xml <- mapper name
												// space

	@Override
	public void insertIparkAccessToken(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		sqlSession.insert(PATH+"insertIparkAccessToken", map);
	}

	@Override
	public String getIparkAccessTokenById(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(PATH+"getIparkAccessTokenById", map);
	}

	@Override
	public List<IparkAccessToken> getAllIparkAccessToken() {
		// TODO Auto-generated method stub
		return sqlSession.selectList(PATH+"getAllIparkAccessToken");
	}

	@Override
	public void updateIparkAccessToken(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		sqlSession.update(PATH+"updateIparkAccessToken", map);
	}

}
