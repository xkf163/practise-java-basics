package ff.project.repository;

import ff.project.entity.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by F on 2017/6/15.
 */
@Repository
public interface FunctionRepository extends JpaRepository<Function,Long> {

}
