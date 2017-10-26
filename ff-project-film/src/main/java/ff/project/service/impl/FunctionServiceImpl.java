package ff.project.service.impl;

import ff.project.entity.Function;
import ff.project.entity.Role;
import ff.project.repository.FunctionRepository;
import ff.project.service.FunctionService;
import ff.project.service.RoleService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by F on 2017/6/15.
 */
@Service
public class FunctionServiceImpl implements FunctionService {

    @Autowired
    FunctionRepository functionRepository;

    @Autowired
    RoleService roleService;

    @Override
    public List<Function> findAll() {
        return functionRepository.findAll();
    }


    @Override
    public Set<String> findCodeSetByRoleCodeSet(Set<String> roleCodes) {

        Set<String> functionCodeSet = new HashSet<String>();
        Iterator<String> iterator = roleCodes.iterator();
        while (iterator.hasNext()){
            String roleCode = iterator.next();
            Role role = roleService.findByCode(roleCode);
            Hibernate.initialize(role.getFunctions());
            Set<Function> functions = role.getFunctions();
            Iterator<Function> functionIterator = functions.iterator();
            while (functionIterator.hasNext()){
                Function function = functionIterator.next();
                functionCodeSet.add(function.getCode());
            }
        }


        return functionCodeSet;
    }
}
