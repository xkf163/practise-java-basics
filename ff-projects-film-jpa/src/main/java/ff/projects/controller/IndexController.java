package ff.projects.controller;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ff.projects.crawler.DouBanPatchProcessor;
import ff.projects.crawler.DouBanProcessor;
import ff.projects.crawler.DouBanSingelProcessor;
import ff.projects.entity.*;
import ff.projects.repository.MediaVORepository;
import ff.projects.service.GatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import us.codecraft.webmagic.Spider;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by F on 2017/6/15.
 */
@Controller
public class IndexController {

    @Autowired
    MediaVORepository mediaVORepository;

    @Autowired
    GatherService gatherService;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DouBanProcessor douBanProcessor;

    @Autowired
    DouBanSingelProcessor douBanSingelProcessor;

    @Autowired
    DouBanPatchProcessor douBanPatchProcessor;

    @RequestMapping(method = RequestMethod.GET,value = "/")
    public String index(HttpServletRequest request){
        return "index";
    }


    /**
     * @Author: xukangfeng
     * @Description 目录树节点URL跳转Controller
     * @Date : 16:33 2017/10/26
     */
    @RequestMapping(method = RequestMethod.GET,value = "/pages/{path}")
    public String pageIndex(Model model,HttpServletRequest request,@PathVariable String path){
        String dataUrl = request.getParameter("dataUrl");
        String toolbarType = request.getParameter("toolbarType");
        model.addAttribute("dataUrl",dataUrl);
        model.addAttribute("toolbarType",toolbarType==null?"toolbar_default":toolbarType);
        return "pages/"+path;
    }


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

    @GetMapping(value = "/transfer")
    @ResponseBody
    public List<MediaVO> transfer(){
        return gatherService.Media2MediaVO();
    }


    @GetMapping(value = "/crawler")
    public String crawler(HttpServletRequest request,@RequestParam (value = "url" ,defaultValue = "https://movie.douban.com") String gatherUrl,
             @RequestParam (value = "thread" ,defaultValue = "1") String thread){
//      Spider.create(douBanProcessor).addUrl("https://movie.douban.com/subject/5308265/?from=aaa").thread(5).run();
        Spider.create(douBanSingelProcessor).addUrl(gatherUrl).thread(Integer.parseInt(thread)).run();
        return "redirect:/";
    }


    @GetMapping(value = "/crawler/patch")
    public String crawlerPatch(HttpServletRequest request,@RequestParam (value = "url",required = false) String gatherUrl,
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

    /**
     * 补救：豆瓣评分 评分人数
     * @param thread
     * @param gatherUrl
     * @return
     */
    @GetMapping(value = "/crawler/patch/1/{thread}")
    public String crawlerPatchA(@PathVariable (name = "thread") String thread,@RequestParam (value = "url",required = false) String gatherUrl){

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


        Spider.create(douBanPatchProcessor).addUrl(urls).thread(Integer.parseInt(thread)).run();
        return "redirect:/";

    }


    @GetMapping(value = "/pickup")
    public String pickUp(){
        gatherService.pickUp();
        return "redirect:/";
    }


    @GetMapping(value = "/relation")
    public String relation(){
        gatherService.relation();
        return "redirect:/";
    }



}
