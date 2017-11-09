package ff.projects.controller;

import com.querydsl.core.types.Predicate;
import ff.projects.entity.*;
import ff.projects.repository.StarRepository;
import ff.projects.service.FilmService;
import ff.projects.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:xukangfeng
 * @Description
 * @Date : Create in 13:15 2017/11/2
 */
@Controller
public class StarController {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    StarRepository starRepository;

    @Autowired
    FilmService filmService;

    Specification<Media> getWhereClause(final HttpServletRequest request){
        //动态查询条件准备
        return new Specification() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {

                String name = request.getParameter("nameExtend");

                List<javax.persistence.criteria.Predicate> predicates = new ArrayList<>();

                if(null!=name && !"".equals(name)){
                    predicates.add(criteriaBuilder.like(root.get("nameExtend"),"%"+name+"%"));
                }
                predicates.add(criteriaBuilder.equal(root.get("deleted"),0));

                return criteriaBuilder.and(predicates.toArray(new javax.persistence.criteria.Predicate[predicates.size()]));
            }
        };
    }


    @PostMapping(value = "/persons")
    @ResponseBody
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


    /**
     * 所有导演列表
     * @param page
     * @param size
     * @param sort
     * @param name
     * @param order
     * @return
     */
    @PostMapping(value = "/person/directors")
    @ResponseBody
    public Page<Star> listAllDirector(@RequestParam(value = "page",defaultValue = "1",required = false) String page,
                                       @RequestParam(value = "rows",defaultValue = "10",required = false) String size,
                                       @RequestParam(value = "sort",defaultValue = "asDirectorNumber",required = false) String sort,
                                       @RequestParam(value = "name",defaultValue = "",required = false) String name,
                                       @RequestParam(value = "order",defaultValue = "DESC",required = false) String order) {

        //排序及分页
        Sort ssort = new Sort(Sort.Direction.ASC,sort);
        if("DESC".equals(order.toUpperCase()))
            ssort = new Sort(Sort.Direction.DESC,sort);
        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size),ssort);

        //查询语句准备
        QStar qStar = QStar.star;
        Predicate predicate= qStar.asDirector.isNotNull();
        //搜索字段.stringValue().isNotEmpty()
        if(null!=name && !"".equals(name)){
            predicate = qStar.asDirector.isNotNull().and(qStar.nameExtend.contains(name));
        }

        return starRepository.findAll(predicate,pageable);
    }


    /**
     * 所有演员列表
     * @param page
     * @param size
     * @param sort
     * @param name
     * @param order
     * @return
     */
    @PostMapping(value = "/person/actors")
    @ResponseBody
    public Page<Star> listAllActor( @RequestParam(value = "page",defaultValue = "1",required = false) String page,
                                             @RequestParam(value = "rows",defaultValue = "10",required = false) String size,
                                             @RequestParam(value = "sort",defaultValue = "asActorNumber",required = false) String sort,
                                             @RequestParam(value = "name",defaultValue = "",required = false) String name,
                                             @RequestParam(value = "order",defaultValue = "DESC",required = false) String order) {
        //排序及分页
        Sort ssort = new Sort(Sort.Direction.ASC,sort);
        if("DESC".equals(order.toUpperCase()))
            ssort = new Sort(Sort.Direction.DESC,sort);
        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size), ssort);
        //查询语句准备
        QStar qStar = QStar.star;
        Predicate predicate= qStar.asActor.isNotNull();
        //搜索字段
        if(null!=name && !"".equals(name)){
            predicate = qStar.asActor.isNotNull().and(qStar.nameExtend.contains(name));
        }
        //Predicate predicate = qPerson.doubanNo.in(JPAExpressions.select(qPersonVOFilmVO.person.id.stringValue()).from(qPersonVOFilmVO).where(qPersonVOFilmVO.asActor.isNotNull()).orderBy(qPersonVOFilmVO.asActor.length().asc()));
        return starRepository.findAll(predicate,pageable);
    }



    /**
     * 人物作品列表
     * @param model
     * @param id
     * @return
     */
    @GetMapping(value = "/persons/{id}")
    public String openPersonResume(Model model, @PathVariable("id") String id){

        Star star = starRepository.findOne(Long.parseLong(id));
        Person person = star.getPerson();
        //参导作品和参演作品s
        List<Film> filmsDirect = filmService.listFilmsByStarId(id,"1");
        List<Film> filmsAct = filmService.listFilmsByStarId(id,"2");
        model.addAttribute("filmsDirect",filmsDirect);
        model.addAttribute("filmsAct",filmsAct);
        model.addAttribute("star",star);
        model.addAttribute("person",person);
        return "pages/info_personResume";

    }

}
