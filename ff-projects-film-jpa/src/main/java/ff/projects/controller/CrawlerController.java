package ff.projects.controller;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ff.projects.crawler.DouBanProcessor;
import ff.projects.entity.Media;
import ff.projects.entity.QMediaVO;
import ff.projects.entity.QMediaVOFilmVO;
import ff.projects.service.GatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import us.codecraft.webmagic.Spider;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:xukangfeng
 * @Description
 * @Date : Create in 11:12 2017/10/27
 */
@Controller
public class CrawlerController {

    @Autowired
    GatherService gatherService;




    @GetMapping(value = "/gather")
    @ResponseBody
    public List<Media> gather() {
        List<Media> lists = new ArrayList<>();
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int intRetVal = jfc.showDialog(new JLabel(), "选择根文件夹");
        File file = jfc.getSelectedFile();
        System.out.println(intRetVal);
        if( intRetVal == JFileChooser.APPROVE_OPTION){
            lists= gatherService.gatherMedia2DB(jfc.getSelectedFile());
        }
        System.out.println("over。。。");
        return lists;
    }



    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DouBanProcessor douBanProcessor;
    /**
      * @Author: xukangfeng
      * @Description
      * @Date : 17:05 2017/10/27
      */
    @GetMapping(value = "/crawler/patch")
    public String crawlerPatch(HttpServletRequest request, @RequestParam(value = "url",required = false) String gatherUrl,
                               @RequestParam (value = "thread" ,defaultValue = "1") String thread){
//      Spider.create(douBanProcessor).addUrl("https://movie.douban.com/subject/5308265/?from=aaa").thread(5).run();

        String rootUrl = "https://movie.douban.com/subject_search?search_text=";
        //查询语句准备
        QMediaVO qMediaVO = QMediaVO.mediaVO;
        QMediaVOFilmVO qMediaVOFilmVO = QMediaVOFilmVO.mediaVOFilmVO;



        Predicate predicate = qMediaVO.id.notIn(JPAExpressions.select(qMediaVOFilmVO.mediaVOId).from(qMediaVOFilmVO)).and(qMediaVO.media.deleted.eq(0));
        List<String> stringList = (List<String>) new JPAQueryFactory(entityManager).selectFrom(qMediaVO).select(qMediaVO.nameChn.prepend(rootUrl)).where(predicate).fetch();

        String[] urls = stringList.toArray(new String[stringList.size()]);

        if(null!=gatherUrl){
            urls = new String[1];
            urls[0]=gatherUrl;
        }

        Spider.create(douBanProcessor).addUrl(urls).thread(Integer.parseInt(thread)).run();
        return "redirect:/";
    }

}
