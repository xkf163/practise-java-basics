package org.ff.service;

import org.ff.config.ShiroRealm;
import org.ff.entity.Function;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface FunctionService {

    List<Function> findAllByOrderByLevelCode();

    Set<String> findUrlSetByRoleCode(Set<String> roleCode);

    List<Function> findFSetByRoleCode(Set<String> roleCodes);
}
