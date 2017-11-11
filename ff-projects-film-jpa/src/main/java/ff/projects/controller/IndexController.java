package ff.projects.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by F on 2017/6/15.
 */
@Controller
public class IndexController {



    @GetMapping(value = "/")
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



/*


    /**
     * @Author: xukangfeng
     * @Description 找出没有Film关联的Media，然后以media。namechn即电影中文名为关键字去豆瓣网搜索电影，再爬取下来
     * @Date : 17:05 2017/10/27
     */
//    @GetMapping(value = "/crawler/patch")
//    public String crawlerPatch(@RequestParam(value = "thread" ,defaultValue = "1") String thread){
//
//        String rootUrl = "https://movie.douban.com/subject_search?search_text=";
//        //查询语句准备
//        QMediaVO qMediaVO = QMediaVO.mediaVO;
//        QMediaVOFilmVO qMediaVOFilmVO = QMediaVOFilmVO.mediaVOFilmVO;
//
//        List<Predicate> predicates = new ArrayList<>();
//        predicates.add(qMediaVO.id.notIn(JPAExpressions.select(qMediaVOFilmVO.mediaVOId).from(qMediaVOFilmVO)));
//        predicates.add(qMediaVO.media.deleted.eq(0));
//        //Predicate predicate = qMediaVO.id.notIn(JPAExpressions.select(qMediaVOFilmVO.mediaVOId).from(qMediaVOFilmVO)).and(qMediaVO.media.deleted.eq(0));
//        List<String> stringList = (List<String>) new JPAQueryFactory(entityManager)
//                .selectFrom(qMediaVO)
//                .select(qMediaVO.nameChn.prepend(rootUrl))
//                .where(predicates.toArray(new Predicate[predicates.size()]))
//                .fetch();
//        //一系列url：格式rootUrl+nameCHN
//        String[] urls = stringList.toArray(new String[stringList.size()]);
//
//        Spider.create(douBanProcessor).addUrl(urls).thread(Integer.parseInt(thread)).run();
//        return "redirect:/";
//    }







    /**
     * 补救：豆瓣评分 评分人数
     * @param thread
     * @param gatherUrl
     * @return
     */
//    @GetMapping(value = "/crawler/patch/1/{thread}")
//    public String crawlerPatchA(@PathVariable(name = "thread") String thread, @RequestParam (value = "url",required = false) String gatherUrl){
//
//        String rootUrl = "https://movie.douban.com/subject/";
//        //查询语句准备
//        QFilm qFilm = QFilm.film;
//        Predicate predicate = qFilm.subjectOther.isNull().or(qFilm.doubanRating.isNull());
//        List<String> stringList = (List<String>) new JPAQueryFactory(entityManager).selectFrom(qFilm).select(qFilm.doubanNo.prepend(rootUrl).append("/")).where(predicate).orderBy(qFilm.year.desc()).fetch();
//        System.out.println("---> "+stringList.size());
//        String[] urls = stringList.toArray(new String[stringList.size()]);
//        if(null!=gatherUrl){
//            urls = new String[1];
//            urls[0]=gatherUrl;
//        }
//
//
//        Spider.create(douBanPatchProcessor).addUrl(urls).thread(Integer.parseInt(thread)).run();
//        return "redirect:/";
//
//    }


}
