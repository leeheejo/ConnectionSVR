package com.icontrols.test.service;

import java.util.HashMap;
import java.util.List;

public interface UserService {
	
	public void insertUser(HashMap<String, Object> map);
	public int loginCheck(HashMap<String, Object> map);
	public List<Object> getUserList();
}
