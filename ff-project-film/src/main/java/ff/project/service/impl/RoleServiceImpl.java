package ff.project.service.impl;

import ff.project.entity.Role;
import ff.project.entity.User;
import ff.project.repository.RoleRepository;
import ff.project.service.RoleService;
import ff.project.service.UserService;
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
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findByCode(String code) {
        return roleRepository.findByCode(code);
    }

    @Override
    public Set<String> findCodeSetByLoginName(String LoginName) {
        User user = userService.findByLoginName(LoginName);
        Hibernate.initialize(user.getRoles());
        Set<Role> roles = user.getRoles();
        Set<String> roleCodeSet = new HashSet<String>();

        Iterator<Role> roleIterator = roles.iterator();
        while (roleIterator.hasNext()){
            Role role = roleIterator.next();
            String roleCode = role.getCode();
            roleCodeSet.add(roleCode);
        }
        return roleCodeSet;
    }
}
