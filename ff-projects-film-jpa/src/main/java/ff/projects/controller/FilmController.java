package ff.projects.controller;

import ff.projects.common.ResultBean;
import ff.projects.service.GatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by F on 2017/11/1.
 */
@Controller
public class FilmController {

    @Autowired
    GatherService gatherService;

    @GetMapping(value = "/connectFilmForMedia")
    public ResultBean<Object[]> connectFilmForMedia(){
        return new ResultBean<Object[]>(gatherService.connectFilmForMedia());
    }
}
