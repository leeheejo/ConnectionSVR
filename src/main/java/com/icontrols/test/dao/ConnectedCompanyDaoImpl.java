package com.icontrols.test.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.icontrols.test.domain.ConnectedCompany;
import com.icontrols.test.domain.Device;

@Repository
public class ConnectedCompanyDaoImpl implements ConnectedCompanyDao {

	@Autowired
	private SqlSessionTemplate sqlSession;

	// mapper's name
	private final String PATH = "ConnectedCompany.";

	@Override
	public List<ConnectedCompany> getConnectedCompany() {
		// TODO Auto-generated method stub
		return sqlSession.selectList(PATH + "getConnectedCompany");
	}
}
