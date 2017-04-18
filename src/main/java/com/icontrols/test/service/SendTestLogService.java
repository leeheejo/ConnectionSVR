package com.icontrols.test.service;

import java.util.List;

import com.icontrols.test.domain.SendTestLog;

public interface SendTestLogService {
	
	public void insertSendTestLog(SendTestLog sendTestLog);
	public List<SendTestLog> getSendTestLog (String uId);

}
