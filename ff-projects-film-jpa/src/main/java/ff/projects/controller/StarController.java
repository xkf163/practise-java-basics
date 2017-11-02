package ff.projects.controller;

import com.querydsl.core.types.Predicate;
import ff.projects.entity.QPerson;
import ff.projects.entity.QStar;
import ff.projects.entity.Star;
import ff.projects.repository.StarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author:xukangfeng
 * @Description
 * @Date : Create in 13:15 2017/11/2
 */
@RestController
public class StarController {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    StarRepository starRepository;

    @PostMapping(value = "/persons")
    public Page<Star> listAll(HttpServletRequest request, @RequestParam(value = "page",defaultValue = "1",required = false) String page,
                                @RequestParam(value = "rows",defaultValue = "10",required = false) String size,
                                @RequestParam(value = "sort",defaultValue = "name",required = false) String sort,
                                @RequestParam(value = "order",defaultValue = "DESC",required = false) String order) {
        //排序及分页
        Sort ssort = new Sort(Sort.Direction.ASC,sort);
        if("DESC".equals(order.toUpperCase()))
            ssort = new Sort(Sort.Direction.DESC,sort);
        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size), ssort);
        return starRepository.findAll(pageable);
    }



    @PostMapping(value = "/person/directors")
    public Page<Star> listAllDirector(HttpServletRequest request, @RequestParam(value = "page",defaultValue = "1",required = false) String page,
                                       @RequestParam(value = "rows",defaultValue = "10",required = false) String size,
                                       @RequestParam(value = "sort",defaultValue = "asDirectorNumber",required = false) String sort,
                                       @RequestParam(value = "order",defaultValue = "DESC",required = false) String order) {

        //排序及分页
        Sort ssort = new Sort(Sort.Direction.ASC,sort);
        if("DESC".equals(order.toUpperCase()))
            ssort = new Sort(Sort.Direction.DESC,sort);

        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size),ssort);
        //查询语句准备
        QStar qStar = QStar.star;
        QPerson qPerson = QPerson.person;
        Predicate predicate= qStar.person.id.eq(qPerson.id).and(qStar.asDirector.stringValue().isNotEmpty()).and(qStar.person.isNotNull());
        return starRepository.findAll(predicate,pageable);
    }


    @PostMapping(value = "/person/actors")
    public Page<Star> listAllActor(HttpServletRequest request, @RequestParam(value = "page",defaultValue = "1",required = false) String page,
                                             @RequestParam(value = "rows",defaultValue = "10",required = false) String size,
                                             @RequestParam(value = "sort",defaultValue = "asActorNumber",required = false) String sort,
                                             @RequestParam(value = "order",defaultValue = "DESC",required = false) String order) {
        //排序及分页
        Sort ssort = new Sort(Sort.Direction.ASC,sort);
        if("DESC".equals(order.toUpperCase()))
            ssort = new Sort(Sort.Direction.DESC,sort);
        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size), ssort);
        //查询语句准备
        QStar qStar = QStar.star;

        Predicate predicate= qStar.asActor.isNotNull();
        //Predicate predicate = qPerson.doubanNo.in(JPAExpressions.select(qPersonVOFilmVO.person.id.stringValue()).from(qPersonVOFilmVO).where(qPersonVOFilmVO.asActor.isNotNull()).orderBy(qPersonVOFilmVO.asActor.length().asc()));
        return starRepository.findAll(predicate,pageable);
    }




}
