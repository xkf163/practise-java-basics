package ff.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by F on 2017/6/15.
 */
@Controller
public class IndexController {

    @RequestMapping(method = RequestMethod.GET,value = "/")
    public String index(HttpServletRequest request){
        return "index";
    }


    @RequestMapping(method = RequestMethod.GET,value = "/pages/{path}")
    public String pageIndex(HttpServletRequest request,@PathVariable String path){
        return "pages/"+path;
    }

}
