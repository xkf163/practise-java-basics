package ff.projects.controller;

import ff.projects.entity.MediaVO;
import ff.projects.repository.MediaVORepository;
import ff.projects.service.GatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
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



    @GetMapping(value = "/transfer")
    @ResponseBody
    public List<MediaVO> transfer(){
        return gatherService.Media2MediaVO();
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
