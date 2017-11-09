package ff.projects.repository;

import ff.projects.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by F on 2017/6/27.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person,Long>, QueryDslPredicateExecutor<Person> {
    Person findByDoubanNo(String doubanNo);


}
