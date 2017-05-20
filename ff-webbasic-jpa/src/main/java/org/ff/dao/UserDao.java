package org.ff.dao;

import org.ff.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Administrator on 2016/12/5 0005.
 */
public interface UserDao extends JpaRepository<User, Integer> {

	@Query("SELECT u FROM User u WHERE u.name=:name")
	User findByName(@Param("name") String name);

	@Query("SELECT u FROM User u WHERE u.loginName=:loginName")
	User findByLoginName(@Param("loginName") String loginName);

	User findById(String id);

}
