package ff.projects.controller;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import ff.projects.entity.Person;
import ff.projects.entity.PersonVOFilmVO;
import ff.projects.entity.QPerson;
import ff.projects.entity.QPersonVOFilmVO;
import ff.projects.repository.PersonRepository;
import ff.projects.repository.PersonVOFilmVORepository;
import ff.projects.service.GatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by F on 2017/6/28.
 */

@RestController
public class PersonController {

    @Autowired
    PersonRepository personRepository;


    @Autowired
    PersonVOFilmVORepository personVOFilmVORepository;

    @Autowired
    GatherService gatherService;

    @PostMapping(value = "/persons")
    public Page<Person> listAll(HttpServletRequest request, @RequestParam(value = "page",defaultValue = "1",required = false) String page,
                                @RequestParam(value = "rows",defaultValue = "10",required = false) String size,
                                @RequestParam(value = "sort",defaultValue = "name",required = false) String sort,
                                @RequestParam(value = "order",defaultValue = "DESC",required = false) String order) {
        //排序及分页
        Sort ssort = new Sort(Sort.Direction.ASC,sort);
        if("DESC".equals(order.toUpperCase()))
            ssort = new Sort(Sort.Direction.DESC,sort);
        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size), ssort);
        //查询语句准备
        QPerson qPerson = QPerson.person;
        QPersonVOFilmVO qPersonVOFilmVO = QPersonVOFilmVO.personVOFilmVO;
        Predicate predicate = qPerson.doubanNo.in(JPAExpressions.select(qPersonVOFilmVO.person.id.stringValue()).from(qPersonVOFilmVO));
        return personRepository.findAll(predicate,pageable);
    }



    @PostMapping(value = "/person/directors")
    public Page<PersonVOFilmVO> listAllDirector(HttpServletRequest request, @RequestParam(value = "page",defaultValue = "1",required = false) String page,
                                @RequestParam(value = "rows",defaultValue = "10",required = false) String size,
                                @RequestParam(value = "sort",defaultValue = "asDirectorNumber",required = false) String sort,
                                @RequestParam(value = "order",defaultValue = "DESC",required = false) String order) {

        //排序及分页
        Sort ssort = new Sort(Sort.Direction.ASC,sort);
        if("DESC".equals(order.toUpperCase()))
            ssort = new Sort(Sort.Direction.DESC,sort);

        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size),ssort);
        //查询语句准备
        QPerson qPerson = QPerson.person;
        QPersonVOFilmVO qPersonVOFilmVO = QPersonVOFilmVO.personVOFilmVO;
        Predicate predicate= qPersonVOFilmVO.person.id.eq(qPerson.id).and(qPersonVOFilmVO.asDirector.isNotNull());
//        Predicate predicate = qPerson.doubanNo.in(JPAExpressions.select(qPersonVOFilmVO.person.id.stringValue()).from(qPersonVOFilmVO).where(qPersonVOFilmVO.asDirector.isNotNull()).orderBy(qPersonVOFilmVO.asDirector.length().asc()));
        return personVOFilmVORepository.findAll(predicate,pageable);
    }


    @PostMapping(value = "/person/actors")
    public Page<PersonVOFilmVO> listAllActor(HttpServletRequest request, @RequestParam(value = "page",defaultValue = "1",required = false) String page,
                                             @RequestParam(value = "rows",defaultValue = "10",required = false) String size,
                                             @RequestParam(value = "sort",defaultValue = "asActorNumber",required = false) String sort,
                                             @RequestParam(value = "order",defaultValue = "DESC",required = false) String order) {
        //排序及分页
        Sort ssort = new Sort(Sort.Direction.ASC,sort);
        if("DESC".equals(order.toUpperCase()))
            ssort = new Sort(Sort.Direction.DESC,sort);
        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size), ssort);
        //查询语句准备
        QPerson qPerson = QPerson.person;
        QPersonVOFilmVO qPersonVOFilmVO = QPersonVOFilmVO.personVOFilmVO;

        Predicate predicate= qPersonVOFilmVO.person.id.eq(qPerson.id).and(qPersonVOFilmVO.asActor.isNotNull());
        //Predicate predicate = qPerson.doubanNo.in(JPAExpressions.select(qPersonVOFilmVO.person.id.stringValue()).from(qPersonVOFilmVO).where(qPersonVOFilmVO.asActor.isNotNull()).orderBy(qPersonVOFilmVO.asActor.length().asc()));
        return personVOFilmVORepository.findAll(predicate,pageable);
    }



}
