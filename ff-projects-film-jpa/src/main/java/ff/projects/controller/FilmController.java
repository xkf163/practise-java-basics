package ff.projects.controller;

import com.querydsl.jpa.JPAExpressions;
import ff.projects.common.ResultBean;
import ff.projects.entity.*;
import ff.projects.repository.FilmRepository;
import ff.projects.repository.MediaRepository;
import ff.projects.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by F on 2017/11/1.
 */
@Controller
public class FilmController {


    @Autowired
    FilmRepository filmRepository ;

    @Autowired
    FilmService filmService;

    @GetMapping(value = "/connectFilmForMedia")
    @ResponseBody
    public ResultBean<Object[]> connectFilmForMedia(){
        return new ResultBean<Object[]>(filmService.connectFilmForMedia());
    }





    Specification<Media> getWhereClause(final HttpServletRequest request){
        //动态查询条件准备
        return new Specification() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                String name = request.getParameter("name");
                String diskNo = request.getParameter("diskNo");
                String startDateStr = request.getParameter("startDate");
                String endDateStr = request.getParameter("endDate");
                String year = (String) request.getAttribute("year");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = null;
                Date endDate =null;
                if(null!=startDateStr && !"".equals(startDateStr)){
                    try {
                        startDate = sdf.parse(startDateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(null!=endDateStr && !"".equals(endDateStr)){
                    try {
                        endDate = sdf.parse(endDateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                List<Predicate> predicates = new ArrayList<>();
                if(null!=startDate){
                    predicates.add(criteriaBuilder.greaterThan(root.get("media").get("gatherDate"),startDate));
                }
                if(null!=endDate){
                    predicates.add(criteriaBuilder.lessThan(root.get("media").get("gatherDate"),endDate));
                }
                if(null!=name && !"".equals(name)){
                    predicates.add(criteriaBuilder.like(root.get("media").get("name"),"%"+name+"%"));
                }
                if(null!=diskNo && !"".equals(diskNo)){
                    predicates.add(criteriaBuilder.equal(root.get("media").get("diskNo"),diskNo.toUpperCase()));
                }
                if(null!=year && !"".equals(year)){
                    predicates.add(criteriaBuilder.equal(root.get("year"),Short.parseShort(year)));
                }
                predicates.add(criteriaBuilder.equal(root.get("media").get("deleted"),0));
                return criteriaBuilder.and(predicates.toArray(new javax.persistence.criteria.Predicate[predicates.size()]));
            }
        };
    }




    @PostMapping(value = "/filmvo/years")
    @ResponseBody
    public Page<Film> listAll(HttpServletRequest request, @RequestParam(value = "page",defaultValue = "1",required = false) String page,
                              @RequestParam(value = "rows",defaultValue = "10",required = false) String size,
                              @RequestParam(value = "sort",defaultValue = "subject",required = false) String sort,
                              @RequestParam(value = "order",defaultValue = "DESC",required = false) String order) {
        //排序及分页
        Sort ssort = new Sort(Sort.Direction.ASC,sort);
        if("DESC".equals(order.toUpperCase()))
            ssort = new Sort(Sort.Direction.DESC,sort);
        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size), ssort);
        //查询语句准备
        QFilm qFilm = QFilm.film;
        QMedia qMedia = QMedia.media;
        com.querydsl.core.types.Predicate predicate = qFilm.id.in(JPAExpressions.select(qMedia.filmId).from(qMedia));
        return filmRepository.findAll(predicate,pageable);


    }

    @Autowired
    MediaRepository mediaRepository;

    /**
     * 根据filmid找到对应media，取出media的fullpath
     * @param filmId
     * @return
     */
    @GetMapping(value = "/films/{id}/media")
    @ResponseBody
    public String listMediaPath(@PathVariable("id") String filmId) {
        //查询语句准备
        QMedia qMedia = QMedia.media;
        com.querydsl.core.types.Predicate predicate = qMedia.filmId.stringValue().eq(filmId);
        Media media = mediaRepository.findOne(predicate);
        if(media != null){
            return media.getDiskNo()+"："+media.getFullPath();
        }
        return "nulll";
    }


    @GetMapping(value = "/persons/{id}/films/type/{type}")
    public String listFilmByPerson(Model model, @PathVariable("id") String id, @PathVariable("type") String type){
        List<Film> filmList = filmService.listFilmsByStarId(id,type);
        model.addAttribute("filmList",filmList);
        return "pages/filmList";

    }

}
