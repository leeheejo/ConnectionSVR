package com.icontrols.test.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icontrols.test.dao.UserDao;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	@Override
	public void insertUser(String uId, String uPwd, String uEmail) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uId", uId);
		map.put("uPwd", uPwd);
		map.put("uEmail", uEmail);
		
		userDao.insertUser(map);
	}
	@Override
	public int loginCheck(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		return userDao.loginCheck(map);
	}
	@Override
	public List<Object> getUserList() {
		// TODO Auto-generated method stub
		return userDao.getUserList();
	}

}
