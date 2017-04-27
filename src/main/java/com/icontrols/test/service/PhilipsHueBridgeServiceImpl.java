package com.icontrols.test.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icontrols.test.dao.PhilipsHueBridgeDao;
import com.icontrols.test.domain.PhilipsHueBridge;

@Service
public class PhilipsHueBridgeServiceImpl implements PhilipsHueBridgeService {

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

	@Override
	public String getPhilipsHueBridgeById(String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("uId", uId);

		return philipsHueBridgeDao.getPhilipsHueBridgeById(map);
	}

	@Override
	public void updatePhilipsHueBridge(String philipsHueBridgeIp, String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("philipsHueBridgeIp", philipsHueBridgeIp);
		map.put("uId", uId);

		philipsHueBridgeDao.updatePhilipsHueBridge(map);
	}

	@Override
	public String getPhilipsHueUsernameById(String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("uId", uId);

		return philipsHueBridgeDao.getPhilipsHueUsernameById(map);
	}

}
