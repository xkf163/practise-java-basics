package ff.project.service;

import ff.project.entity.Role;

import java.util.List;
import java.util.Set;

/**
 * Created by F on 2017/6/15.
 */
public interface RoleService {
    List<Role> findAll();

    Set<String> findCodeSetByLoginName(String LoginName);

    Role findByCode(String code);
}
