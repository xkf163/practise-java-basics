package ff.projects.repository;

import ff.projects.entity.PersonVOFilmVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by F on 2017/6/28.
 */

@Repository
public interface PersonVOFilmVORepository extends JpaRepository<PersonVOFilmVO,Long> ,QueryDslPredicateExecutor<PersonVOFilmVO> {
}
