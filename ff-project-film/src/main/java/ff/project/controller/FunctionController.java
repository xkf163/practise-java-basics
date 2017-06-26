package ff.project.controller;

import ff.project.entity.Function;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by F on 2017/6/15.
 */
@Controller
@RequestMapping(value = "/function")
public class FunctionController {

    /**
     *
     * @return
     */
    @RequestMapping(value = "/all" , method = RequestMethod.POST)
    public List<Function> getALl(){

        return null;
    }




}



