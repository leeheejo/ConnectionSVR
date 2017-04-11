package com.icontrols.test.dao;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

@Repository(value="SendTestLog")
public interface  SendTestLogDao{
	
	public void insertSendTestLog(HashMap<String, Object> map);

}
