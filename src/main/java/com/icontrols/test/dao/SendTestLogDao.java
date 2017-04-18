package com.icontrols.test.dao;

import java.util.HashMap;
import java.util.List;


import com.icontrols.test.domain.SendTestLog;

public interface  SendTestLogDao{
	
	public void insertSendTestLog(HashMap<String, Object> map);
	public List<SendTestLog> getSendTestLog(HashMap<String, Object> map);

}
