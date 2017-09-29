package ff.projects.controller;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import ff.projects.entity.*;
import ff.projects.repository.FilmRepository;
import ff.projects.repository.MediaRepository;
import ff.projects.repository.MediaVOFilmVORepository;
import ff.projects.repository.MediaVORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by F on 2017/6/28.
 */
@Controller
public class FilmVOController {

@Autowired
MediaRepository mediaRepository;

    @Autowired
    MediaVORepository mediaVORepository;

    @Autowired
    FilmRepository  filmRepository ;

    @Autowired
    MediaVOFilmVORepository mediaVOFilmVORepository;

    Specification<MediaVO> getWhereClause(final HttpServletRequest request){
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
                List<javax.persistence.criteria.Predicate> predicates = new ArrayList<>();
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
        QMediaVOFilmVO qMediaVOFilmVO = QMediaVOFilmVO.mediaVOFilmVO;
        Predicate predicate = qFilm.id.in(JPAExpressions.select(qMediaVOFilmVO.filmVOId).from(qMediaVOFilmVO));
        return filmRepository.findAll(predicate,pageable);


    }


    @GetMapping(value = "/films/{id}/media")
    @ResponseBody
    public String listMediaPath(@PathVariable("id") String id) {
        //查询语句准备
        QMediaVOFilmVO qMediaVOFilmVO = QMediaVOFilmVO.mediaVOFilmVO;
        QMediaVO qMediaVO = QMediaVO.mediaVO;
        Film f = filmRepository.findOne(Long.parseLong(id));
        Predicate predicate = qMediaVO.id.in(JPAExpressions.selectFrom(qMediaVOFilmVO).select(qMediaVOFilmVO.mediaVOId).where(qMediaVOFilmVO.filmVOId.stringValue().eq(id)));
        List<MediaVO> mediaVOList = (List<MediaVO>) mediaVORepository.findAll(predicate);
        if(mediaVOList.size()>0){
            return mediaVOList.get(0).getMedia().getDiskNo()+"："+mediaVOList.get(0).getMedia().getFullPath();
        }
        return "nulll";
    }




}
