package ff.projects.controller;

import ff.projects.common.ResultBean;
import ff.projects.service.FilmService;
import ff.projects.service.GatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by F on 2017/11/1.
 */
@Controller
public class FilmController {

    @Autowired
    FilmService filmService;

    @GetMapping(value = "/connectFilmForMedia")
    @ResponseBody
    public ResultBean<Object[]> connectFilmForMedia(){
        return new ResultBean<Object[]>(filmService.connectFilmForMedia());
    }
}
