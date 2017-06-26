package ff.project.repository;

import ff.project.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by F on 2017/6/15.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{
    Role findByCode(String code);
}
