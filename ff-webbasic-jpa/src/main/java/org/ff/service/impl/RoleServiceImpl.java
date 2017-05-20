package org.ff.service.impl;


import org.ff.dao.RoleDao;
import org.ff.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("roleService")
public class RoleServiceImpl  implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public Set<String> findRoleCodeSetByUserName(String username) {
        return roleDao.findRoleCodeSetByUserName(username);
    }

    @Override
    public Set<String> findRoleIdSetByUserName(String username) {
        return roleDao.findRoleIdSetByUserName(username);
    }

}
