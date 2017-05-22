package org.ff.dao;

import org.ff.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * Created by 57257 on 2017/5/20.
 */
public interface RoleDao extends JpaRepository<Role,Integer>{

    @Query(value = "select distinct r.code from tbl_user_role ur left join tbl_role r on ur.roleId=r.id left join tbl_user u on ur.userId=u.id where u.name=?1", nativeQuery = true)
    Set<String> findRoleCodeSetByUserName(String username);

    @Query(value = "select distinct r.roleId from tbl_user_role ur left join tbl_role r on ur.roleId=r.id left join tbl_user u on ur.userId=u.id where u.name=?1", nativeQuery = true)
    Set<String> findRoleIdSetByUserName(String username);


}
