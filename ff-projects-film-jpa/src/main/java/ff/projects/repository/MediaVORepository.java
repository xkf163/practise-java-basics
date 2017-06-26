package ff.projects.repository;

import ff.projects.entity.MediaVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by F on 2017/6/22.
 */
@Repository
public interface MediaVORepository extends JpaRepository<MediaVO,Long>,QueryDslPredicateExecutor<MediaVO>,JpaSpecificationExecutor {

    Page<MediaVO> findByYear(Short year, Pageable pageable);
}
