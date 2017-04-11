package com.icontrols.test.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository(value="User")
public interface UserDao {
	
	public void insertUser(HashMap<String, Object> map);
	public int loginCheck(HashMap<String, Object> map);
	public List<Object> getUserList();

}
