package com.icontrols.test.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	//mapper's name 
	private final String PATH = "User."; 

	/*  call sqlSession
	 *  @RequestParam map
	 */
	@Override
	public void insertUser(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		
		/*  access to the mapper
		 *  @RequestParam statement mapper's name + statement's id
		 *  @RequestParam parameter (for VALUES clause of the INSERT statement)
		 */
		sqlSession.insert(PATH + "insertUser", map);
	}

	
	/*  call sqlSession
	 *  @RequestParam map
	 */
	@Override
	public int loginCheck(HashMap<String, Object> map) {
		// TODO Auto-generated method stub

		/*  access to the mapper
		 *  @RequestParam statement statement mapper's name + statement's id
		 *  @RequestParam parameter (for WHERE clause of the SELECT statement)
		 */
		return sqlSession.selectOne(PATH + "loginCheck", map);
	}


	@Override
	public List<Object> getUserList() {
		// TODO Auto-generated method stub
		return sqlSession.selectList(PATH + "getUserList");

	}

}
