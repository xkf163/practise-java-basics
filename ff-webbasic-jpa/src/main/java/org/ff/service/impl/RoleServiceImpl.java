package org.ff.service.impl;


import org.ff.service.RoleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("roleService")
public class RoleServiceImpl  implements RoleService {


    @Value("${shiro.sql.roles}")
    private String shiroSqlRoles;

    @Override
    public Set<String> getRoleCodeSet(String username) {
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("param",username);
//        List list=super.findMapBySql(shiroSqlRoles,params);
        List list=null;
        Set<String> retSet=new HashSet<String>();
        retSet.addAll(list);
        return retSet;
    }
}
