package com.icontrols.test.dao;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

public interface ArtikUserProfileDao {
	
	public void insertArtikUserProfile(HashMap<String, Object> hashmap);
	public void updateArtikUserProfile(HashMap<String, Object> hashmap);
	public String getUserIdById(HashMap<String, Object> map);
	public int artikUserCheck(HashMap<String, Object> map);
}
