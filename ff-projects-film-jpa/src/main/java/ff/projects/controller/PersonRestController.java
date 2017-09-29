package ff.projects.controller;

import ff.projects.entity.Film;
import ff.projects.repository.PersonRepository;
import ff.projects.repository.PersonVOFilmVORepository;
import ff.projects.service.GatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Created by F on 2017/7/1.
 */
@Controller
public class PersonRestController {


    @Autowired
    PersonRepository personRepository;


    @Autowired
    PersonVOFilmVORepository personVOFilmVORepository;

    @Autowired
    GatherService gatherService;

    @GetMapping(value = "/persons/{id}/films/type/{type}")
    public String listFilmByPerson(Model model, @PathVariable("id") String id, @PathVariable("type") String type){
        List<Film> filmList = gatherService.listFilmsByPersonId(id,type);
        model.addAttribute("filmList",filmList);
        return "pages/filmList";

    }
}
