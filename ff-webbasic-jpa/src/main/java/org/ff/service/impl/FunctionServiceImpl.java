package org.ff.service.impl;

import org.ff.dao.FunctionDao;
import org.ff.entity.Function;
import org.ff.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("functionService")
public class FunctionServiceImpl implements FunctionService {

    @Autowired
    private FunctionDao functionDao;


    @Override
    public List<Function> findAllByOrderByLevelCode(){
        return functionDao.findAllByOrderByLevelCode();
    }

    @Override
    public Set<String> findUrlSetByRoleCode(Set<String> roleCode){
        return functionDao.findUrlSetByRoleCode(roleCode);
    }

    @Override
    public List<Function> findFSetByRoleCode(Set<String> roleCodes){
        return functionDao.findFSetByRoleCode(roleCodes);
    }

}
