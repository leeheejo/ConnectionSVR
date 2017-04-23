package com.icontrols.test.service;

import java.util.HashMap;
import java.util.List;

public interface UserService {
	
	public void insertUser(String uId, String uPwd, String uEmail);
	public int loginCheck(HashMap<String, Object> map);
	public List<Object> getUserList();
}
