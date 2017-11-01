package ff.projects.restcontroller;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ff.projects.common.ResultBean;
import ff.projects.crawler.DouBanProcessor;
import ff.projects.entity.Media;
import ff.projects.entity.QFilm;
import ff.projects.entity.QMediaVO;
import ff.projects.entity.QMediaVOFilmVO;
import ff.projects.service.CrawlerService;
import ff.projects.service.GatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import us.codecraft.webmagic.Spider;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:xukangfeng
 * @Description
 * @Date : Create in 11:12 2017/10/27
 */
@RestController
public class CrawlerController {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DouBanProcessor douBanProcessor;

    @Autowired
    CrawlerService crawlerService;


    /**
     * @Author: xukangfeng
     * @Description 通用爬取方法
     * @Date : 22:05 2017/10/27
     * @param : url 豆瓣单个电影页面地址
     * @param : thread 线程数
     */
    @PostMapping(value = "/crawling")
    public ResultBean<Object[]> crawler(@RequestParam (value = "url" ,required = true) String singleFilmUrl,
                              @RequestParam (value = "mutil" ,defaultValue = "0") String mutil,
                              @RequestParam (value = "homepage" ,defaultValue = "0") String homepage,
                              @RequestParam (value = "thread" ,defaultValue = "1") String thread){

        return new ResultBean<Object[]>(crawlerService.running(mutil,singleFilmUrl,thread,homepage));
    }



    /**
      * @Author: xukangfeng
      * @Description 找出没有Film关联的Media，然后以media。namechn即电影中文名为关键字去豆瓣网搜索电影，再爬取下来
      * @Date : 17:05 2017/10/27
      */
    @GetMapping(value = "/crawler/patch")
    public String crawlerPatch(@RequestParam (value = "thread" ,defaultValue = "1") String thread){

        String rootUrl = "https://movie.douban.com/subject_search?search_text=";
        //查询语句准备
        QMediaVO qMediaVO = QMediaVO.mediaVO;
        QMediaVOFilmVO qMediaVOFilmVO = QMediaVOFilmVO.mediaVOFilmVO;

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(qMediaVO.id.notIn(JPAExpressions.select(qMediaVOFilmVO.mediaVOId).from(qMediaVOFilmVO)));
        predicates.add(qMediaVO.media.deleted.eq(0));
        //Predicate predicate = qMediaVO.id.notIn(JPAExpressions.select(qMediaVOFilmVO.mediaVOId).from(qMediaVOFilmVO)).and(qMediaVO.media.deleted.eq(0));
        List<String> stringList = (List<String>) new JPAQueryFactory(entityManager)
                .selectFrom(qMediaVO)
                .select(qMediaVO.nameChn.prepend(rootUrl))
                .where(predicates.toArray(new Predicate[predicates.size()]))
                .fetch();
        //一系列url：格式rootUrl+nameCHN
        String[] urls = stringList.toArray(new String[stringList.size()]);

        Spider.create(douBanProcessor).addUrl(urls).thread(Integer.parseInt(thread)).run();
        return "redirect:/";
    }







    /**
     * 补救：豆瓣评分 评分人数
     * @param thread
     * @param gatherUrl
     * @return
     */
    @GetMapping(value = "/crawler/patch/1/{thread}")
    public String crawlerPatchA(@PathVariable(name = "thread") String thread, @RequestParam (value = "url",required = false) String gatherUrl){

        String rootUrl = "https://movie.douban.com/subject/";
        //查询语句准备
        QFilm qFilm = QFilm.film;
        Predicate predicate = qFilm.subjectOther.isNull().or(qFilm.doubanRating.isNull());
        List<String> stringList = (List<String>) new JPAQueryFactory(entityManager).selectFrom(qFilm).select(qFilm.doubanNo.prepend(rootUrl).append("/")).where(predicate).orderBy(qFilm.year.desc()).fetch();
        System.out.println("---> "+stringList.size());
        String[] urls = stringList.toArray(new String[stringList.size()]);
        if(null!=gatherUrl){
            urls = new String[1];
            urls[0]=gatherUrl;
        }


        Spider.create(douBanProcessor).addUrl(urls).thread(Integer.parseInt(thread)).run();
        return "redirect:/";

    }

}