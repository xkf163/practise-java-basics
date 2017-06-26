package ff.project.repository;

import ff.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by F on 2017/6/16.
 */

public interface UserRepository extends JpaRepository<User,Long> {

    User findByLoginName(String loginName);
}
