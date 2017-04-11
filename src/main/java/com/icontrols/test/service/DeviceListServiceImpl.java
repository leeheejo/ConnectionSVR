package com.icontrols.test.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icontrols.test.dao.DeviceListDao;
import com.icontrols.test.domain.DeviceList;

@Service
public class DeviceListServiceImpl implements DeviceListService {

	@Autowired
	private DeviceListDao deviceListDao;

	@Override
	public void inserDeviceList(DeviceList deviceList) {
		// TODO Auto-generated method stub

		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("uId", deviceList.getuId());
		map.put("id", deviceList.getId());
		map.put("dtid", deviceList.getDtid());
		map.put("name", deviceList.getName());
		map.put("manifestVersion", deviceList.getManifestVersion());
		map.put("manifestVersionPolicy", deviceList.getManifestVersionPolicy());
		map.put("needProviderAuth", deviceList.isNeedProviderAuth());
		map.put("cloudAuthorization", deviceList.getCloudAuthorization());
		map.put("createdOn", deviceList.getCreatedOn());
		map.put("connected", deviceList.isConnected());
		map.put("sharedWithOthers", deviceList.isSharedWithOthers());
		map.put("sharedWithMe", deviceList.getSharedWithMe());

		deviceListDao.inserDeviceList(map);

	}

}
