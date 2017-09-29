package ff.projects.repository;

import ff.projects.entity.MediaVOFilmVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by F on 2017/6/28.
 */
@Repository
public interface MediaVOFilmVORepository extends JpaRepository<MediaVOFilmVO,Long>, QueryDslPredicateExecutor<MediaVOFilmVO> {
}
