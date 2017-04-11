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
	public void insertUser(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
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
