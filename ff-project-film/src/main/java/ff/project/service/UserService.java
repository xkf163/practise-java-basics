package ff.project.service;

import ff.project.entity.User;

/**
 * Created by F on 2017/6/16.
 */
public interface UserService {
    User findByLoginName(String loginName);
}
