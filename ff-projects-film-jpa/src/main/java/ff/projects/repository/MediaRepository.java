package ff.projects.repository;

import ff.projects.entity.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by F on 2017/6/20.
 */
@Repository
public interface MediaRepository extends JpaRepository<Media,Long>, QueryDslPredicateExecutor<Media>, JpaSpecificationExecutor {
    Page<Media> findByYear(Short year, Pageable pageable);
}
