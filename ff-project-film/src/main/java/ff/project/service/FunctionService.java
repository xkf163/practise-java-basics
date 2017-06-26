package ff.project.service;

import ff.project.entity.Function;

import java.util.List;
import java.util.Set;

/**
 * Created by F on 2017/6/15.
 */
public interface FunctionService {
    List<Function> findAll();

    Set<String> findCodeSetByRoleCodeSet(Set<String> roleCodes);
}
