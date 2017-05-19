package org.ff.service;

import org.ff.entity.User;

import java.util.List;

/**
 * Created by F on 2017/5/18.
 */
public interface UserService {

    /**
     * 根据用户名查询用户信息
     */
    User findByName(String name);


    User findByLoginName(String loginName);

    /**
     * 查询所有的用户信息
     */
    List<User> findAll();

}
