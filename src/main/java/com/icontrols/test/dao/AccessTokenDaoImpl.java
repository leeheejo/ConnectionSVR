package com.icontrols.test.dao;

import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AccessTokenDaoImpl implements AccessTokenDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	private final String PATH = "AccessToken.";

	@Override
	public void insertAccessToken(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		sqlSession.insert(PATH + "insertAccessToken", map);
	}

	@Override
	public int tokenCheck(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(PATH + "tokenCheck", map);
	}

	@Override
	public void updateAccessToken(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		
		sqlSession.update(PATH + "updateAccessToken", map);
		
	}

	@Override
	public String getAccessTokenById(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(PATH+"getAccessTokenById", map);
	}

}
