package ff.projects.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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





}
