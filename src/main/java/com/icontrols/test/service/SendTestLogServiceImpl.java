package com.icontrols.test.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icontrols.test.dao.SendTestLogDao;
import com.icontrols.test.domain.SendTestLog;

@Service
public class SendTestLogServiceImpl implements SendTestLogService{

	@Autowired
	SendTestLogDao sendTestLogDao;
	
	@Override
	public void insertSendTestLog(SendTestLog sendTestLog) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("uId", sendTestLog.getuId());
		map.put("cmpCode", sendTestLog.getCmpCode());
		map.put("ddid", sendTestLog.getDdid());
		map.put("data", sendTestLog.getData());
		map.put("response", sendTestLog.getResponse());
		map.put("sendDt", sendTestLog.getSendDt());
		
		sendTestLogDao.insertSendTestLog(map);
	}

	@Override
	public List<SendTestLog> getSendTestLog(String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("uId", uId);
		
		return sendTestLogDao.getSendTestLog(map);
	}

}
