package com.icontrols.test.service;

import com.icontrols.test.domain.ArtikUserProfile;

public interface ArtikUserProfileService {
	
	public void insertArtikUserProfile(ArtikUserProfile artikUserProfile);
	public void updateArtikUserProfile(ArtikUserProfile artikUserProfile);
	public String getUserIdById(String uId);
	public int artikUserCheck(String uId);
}
