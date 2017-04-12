package com.icontrols.test.dao;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

public interface  SendTestLogDao{
	
	public void insertSendTestLog(HashMap<String, Object> map);

}
