package com.icontrols.test.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icontrols.test.dao.ArtikUserProfileDao;
import com.icontrols.test.domain.ArtikUserProfile;

@Service
public class ArtikUserProfileServiceImpl implements ArtikUserProfileService {

	@Autowired
	ArtikUserProfileDao artikUserProfileDao;

	@Override
	public void insertArtikUserProfile(ArtikUserProfile artikUserProfile) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("uId", artikUserProfile.getuId());
		map.put("id", artikUserProfile.getId());
		map.put("name", artikUserProfile.getName());
		map.put("email", artikUserProfile.getEmail());
		map.put("fullName", artikUserProfile.getFullName());
		map.put("saIdentity", artikUserProfile.getSaIdentity());
		map.put("accountType", artikUserProfile.getAccountType());
		map.put("createdOn", artikUserProfile.getCreatedOn());
		map.put("modifiedOn", artikUserProfile.getModifiedOn());

		artikUserProfileDao.insertArtikUserProfile(map);
	}

	@Override
	public void updateArtikUserProfile(ArtikUserProfile artikUserProfile) {
		// TODO Auto-generated method stub

		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("uId", artikUserProfile.getuId());
		map.put("id", artikUserProfile.getId());
		map.put("name", artikUserProfile.getName());
		map.put("email", artikUserProfile.getEmail());
		map.put("fullName", artikUserProfile.getFullName());
		map.put("saIdentity", artikUserProfile.getSaIdentity());
		map.put("accountType", artikUserProfile.getAccountType());
		map.put("createdOn", artikUserProfile.getCreatedOn());
		map.put("modifiedOn", artikUserProfile.getModifiedOn());

		artikUserProfileDao.updateArtikUserProfile(map);

	}

	@Override
	public String getUserIdById(String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uId", uId);
		return artikUserProfileDao.getUserIdById(map);
	}

	@Override
	public int artikUserCheck(String uId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uId", uId);
		return artikUserProfileDao.artikUserCheck(map);
	}

}
