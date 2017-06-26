package ff.project.service.impl;

import ff.project.entity.User;
import ff.project.repository.UserRepository;
import ff.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by F on 2017/6/16.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

//    @Query("select a.name from User a where a.loginName = ?1")
//@Query("SELECT u FROM User u WHERE u.loginName=:loginName")
    public User findByLoginName(String loginName){
        return userRepository.findByLoginName(loginName);
    }

}
