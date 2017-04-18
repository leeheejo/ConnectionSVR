package com.icontrols.test.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.icontrols.test.domain.SendTestLog;

@Repository
public class SendTestLogDaoImpl implements SendTestLogDao {

	@Autowired
	private SqlSessionTemplate sqlSession;

	// mapper's name
	private final String PATH = "SendTestLog.";

	@Override
	public void insertSendTestLog(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		sqlSession.insert(PATH + "insertSendTestLog", map);
	}

	@Override
	public List<SendTestLog> getSendTestLog(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectList(PATH + "getSendTestLog", map);
	}

}
