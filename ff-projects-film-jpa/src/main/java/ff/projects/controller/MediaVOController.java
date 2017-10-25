package ff.projects.controller;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ff.projects.entity.*;
import ff.projects.repository.MediaVORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
 * Created by F on 2017/6/23.
 */
@RestController
public class MediaVOController {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    MediaVORepository mediaVORepository;

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




    @PostMapping(value = "/mediavo/years")
    public Page<MediaVO> listAll(HttpServletRequest request, @RequestParam(value = "page",defaultValue = "1",required = false) String page,
                                 @RequestParam(value = "rows",defaultValue = "10",required = false) String size,
                                 @RequestParam(value = "sort",defaultValue = "year",required = false) String sort,
                                 @RequestParam(value = "order",defaultValue = "DESC",required = false) String order) {
        //排序及分页
        Sort ssort = new Sort(Sort.Direction.ASC,sort);
        if("DESC".equals(order.toUpperCase()))
            ssort = new Sort(Sort.Direction.DESC,sort);
        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size), ssort);
        //查询语句准备
        Specification querySpecifi = getWhereClause(request);
        return mediaVORepository.findAll(querySpecifi,pageable);
    }


    @PostMapping(value = "/mediavo/years/{year}")
    public Page<MediaVO> listByYear(HttpServletRequest request,@PathVariable("year") String year, @RequestParam(value = "page",defaultValue = "1",required = false) String page, @RequestParam(value = "rows",defaultValue = "10",required = false) String size){
        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size), new Sort(Sort.Direction.ASC,"nameChn"));
        request.setAttribute("year",year);
        //查询语句准备
        Specification querySpecifi = getWhereClause(request);
        return mediaVORepository.findAll(querySpecifi,pageable);
    }

    @PostMapping(value = "/mediavo/gatherdates")
    public Page<MediaVO> listByGatherDates(HttpServletRequest request, @RequestParam(value = "page",defaultValue = "1",required = false) String page, @RequestParam(value = "rows",defaultValue = "10",required = false) String size){
        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size), new Sort(Sort.Direction.DESC,"media.gatherDate"));
        //查询语句准备
        Specification querySpecifi = getWhereClause(request);
        return mediaVORepository.findAll(querySpecifi, pageable);
    }


    @PostMapping(value = "/mediavo/gatherdate/{yearMonth}")
    public Page<MediaVO> listByGatherDate(HttpServletRequest request,@PathVariable("yearMonth") String yearMonth,@RequestParam(value = "page",defaultValue = "1",required = false) String page, @RequestParam(value = "rows",defaultValue = "10",required = false) String size) throws ParseException {
        String year = yearMonth.substring(0,4);
        String month = yearMonth.substring(4);
        yearMonth = year+"-"+month+"-";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(yearMonth+"01");
        Date endDate = sdf.parse(yearMonth+lastDate(year,month));

        //搜索区传入的参数值
        String name = request.getParameter("name");
        String diskNo = request.getParameter("diskNo");

        /*
         * queryDsl
         */
        QMedia media = QMedia.media;
        QMediaVO mediaVO = QMediaVO.mediaVO;
        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size), new Sort(Sort.Direction.DESC,"media.gatherDate"));
        //该Predicate为querydsl下的类,支持嵌套组装复杂查询条件
        Predicate predicate = mediaVO.media.gatherDate.before(endDate).and(mediaVO.media.gatherDate.after(startDate)).and(mediaVO.media.deleted.eq(0));

//        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
//        List<MediaVO> list = queryFactory.selectFrom(media)
//                .leftJoin(meidaVO)
//                .on(media.id.eq(meidaVO.media.id))
//                .select(meidaVO)
//                .where(media.gatherDate.before(endDate),media.gatherDate.after(startDate))
//                .orderBy(media.gatherDate.desc())
//                .fetch();
       return mediaVORepository.findAll(predicate,pageable);
    }


    /**
     * 查询name重复的数据
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/mediavo/repetitive")
    public Page<MediaVO> listRepetitive(@RequestParam(value = "page",defaultValue = "1",required = false) String page, @RequestParam(value = "rows",defaultValue = "10",required = false) String size){
        QMediaVO mediaVO = QMediaVO.mediaVO;
//        QMedia media = QMedia.media;
        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size), new Sort(Sort.Direction.DESC,"nameChn"));
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        List<String> listRepeat = queryFactory.selectFrom(mediaVO)
                .groupBy(mediaVO.nameChn,mediaVO.year)
                .select(mediaVO.nameChn)
                .where(mediaVO.media.deleted.eq(0))
                .having(mediaVO.nameChn.count().gt(1))
                .fetch();

        Predicate predicate = mediaVO.nameChn.in(listRepeat).and(mediaVO.media.deleted.eq(0));
        return mediaVORepository.findAll(predicate,pageable);
    }

    /**
     * 展示带删除标记的数据
     * @param deleted
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/mediavo/deleted/{deleted}")
    public Page<MediaVO> listDeleted(@PathVariable("deleted") String deleted,
                            @RequestParam(value = "page",defaultValue = "1",required = false) String page,
                            @RequestParam(value = "rows",defaultValue = "10",required = false) String size){
        QMediaVO mediaVO = QMediaVO.mediaVO;
        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size), new Sort(Sort.Direction.DESC,"media.gatherDate"));
        Predicate predicate = mediaVO.media.deleted.eq(Integer.parseInt(deleted));
        return mediaVORepository.findAll(predicate,pageable);
    }




    @PostMapping(value = "/mediavo/unrelation/")
    public Page<MediaVO> listAllUnRelation(HttpServletRequest request, @RequestParam(value = "page",defaultValue = "1",required = false) String page,
                              @RequestParam(value = "rows",defaultValue = "10",required = false) String size,
                              @RequestParam(value = "sort",defaultValue = "nameChn",required = false) String sort,
                              @RequestParam(value = "order",defaultValue = "DESC",required = false) String order) {
        //排序及分页
        Sort ssort = new Sort(Sort.Direction.ASC,sort);
        if("DESC".equals(order.toUpperCase()))
            ssort = new Sort(Sort.Direction.DESC,sort);
        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size), ssort);
        //查询语句准备
        QMediaVO qMediaVO = QMediaVO.mediaVO;
        QMediaVOFilmVO qMediaVOFilmVO = QMediaVOFilmVO.mediaVOFilmVO;
        Predicate predicate = qMediaVO.id.notIn(JPAExpressions.select(qMediaVOFilmVO.mediaVOId).from(qMediaVOFilmVO)).and(qMediaVO.media.deleted.eq(0));
        return mediaVORepository.findAll(predicate,pageable);


    }

    //判断是不是闰年
    public boolean isLeapYear(int year){
        if (year % 4 != 0)
            return false;
        if (year % 400 == 0)
            return true;
        return year % 100 != 0;
    }

    //取出最大日期
    public String lastDate(String yearStr,String monthStr){
        int year = Integer.parseInt(yearStr);
        int month = Integer.parseInt(monthStr);
        int kLastDates[] = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        if(month>12 || month<0){
            month = 0;
        }
        if(month==2 && isLeapYear(year)){
            return String.valueOf(kLastDates[month] + 1);
        }else{
            return String.valueOf(kLastDates[month]);
        }
    }

}
