package com.icontrols.test.dao;

import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ArtikUserProfileDaoImpl implements ArtikUserProfileDao {

	@Autowired
	private SqlSessionTemplate sqlSession;

	// mapper's name
	private final String PATH = "ArtikUserProfile.";

	/*
	 * call sqlSession
	 * 
	 * @RequestParam map
	 */
	@Override
	public void insertArtikUserProfile(HashMap<String, Object> map) {
		// TODO Auto-generated method stub

		/*
		 * access to the mapper
		 * 
		 * @RequestParam statement mapper's name + statement's id
		 * 
		 * @RequestParam parameter (for VALUES clause of the INSERT statement)
		 */
		sqlSession.insert(PATH + "insertArtikUserProfile", map);
	}

	@Override
	public void updateArtikUserProfile(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		sqlSession.insert(PATH + "updateArtikUserProfile", map);
	}

	@Override
	public String getUserIdById(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(PATH + "getUserIdById", map);
	}

	@Override
	public int artikUserCheck(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(PATH + "artikUserCheck", map);
	}

}
