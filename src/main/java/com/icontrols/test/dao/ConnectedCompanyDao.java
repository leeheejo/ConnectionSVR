package com.icontrols.test.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.icontrols.test.domain.ConnectedCompany;

public interface ConnectedCompanyDao {
	
	public List<ConnectedCompany> getConnectedCompany();

}
