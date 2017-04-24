package com.icontrols.test.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icontrols.test.dao.PhilipsHueBridgeDao;

@Service
public class PhilipsHueBridgeServiceImpl implements PhilipsHueBridgeService{

	@Autowired 
	PhilipsHueBridgeDao philipsHueBridgeDao;
	
	@Override
	public void insertPhilipsHueBridge(String philipsHueBridgeIp, String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("philipsHueBridgeIp", philipsHueBridgeIp);
		map.put("uId", uId);
		
		philipsHueBridgeDao.insertPhilipsHueBridge(map);
		
	}

}
