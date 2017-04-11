package com.icontrols.test.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icontrols.test.dao.ConnectedCompanyDao;
import com.icontrols.test.domain.ConnectedCompany;

@Service
public class ConnectedCompanyServiceImpl implements ConnectedCompanyService{

	@Autowired 
	ConnectedCompanyDao connectedCompanyDao;
	
	@Override
	public List<ConnectedCompany> getConnectedCompany() {
		// TODO Auto-generated method stub
		return connectedCompanyDao.getConnectedCompany();
	}

}
