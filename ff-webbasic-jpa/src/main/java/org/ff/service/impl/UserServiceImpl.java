package org.ff.service.impl;

import org.ff.dao.UserDao;
import org.ff.entity.User;
import org.ff.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/12/5 0005.
 */
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	@Override
	public User findByName(String name) {
		return userDao.findByName(name);
	}

	@Override
	public User findByLoginName(String loginName){return userDao.findByLoginName(loginName);}

	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}
}
