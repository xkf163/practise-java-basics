package org.ff.dao;

import org.ff.entity.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by 57257 on 2017/5/20.
 */
@Repository
public interface FunctionDao extends JpaRepository<Function , Integer> {


    List<Function> findAllByOrderByLevelCode();


    @Query(value = "select distinct f.code from tbl_function f where f.code=?1", nativeQuery = true)
    Set<String> findCodeSetByRoleCode(String roleCode);


    @Query(value = "select f.url from tbl_function f left join tbl_role_function rf on f.id = rf.functionId where rf.roleId in ( SELECT r.id FROM tbl_role r WHERE r.code in ?1)", nativeQuery = true)
    Set<String> findUrlSetByRoleCode(Set<String> roleCode);

    @Query(value = "select f.* from tbl_function f left join tbl_role_function rf on f.id = rf.functionId where rf.roleId in ( SELECT r.id FROM tbl_role r WHERE r.code in ?1)", nativeQuery = true)
    List<Function> findFSetByRoleCode(Set<String> roleCodes);
}
