package ff.projects.controller;

import ff.projects.entity.Media;
import ff.projects.repository.MediaRepository;
import ff.projects.repository.StarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author:xukangfeng
 * @Description
 * @Date : Create in 16:26 2018/2/1
 */

@Controller
public class MediaController {

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    StarRepository starRepository;
    /**
     *
     * @return
     */
    @GetMapping(value = "/media/{mid}")
    public String mediaShow(Model model, @PathVariable Long mid){
        Media media = mediaRepository.findOne(mid);
        model.addAttribute(media);

        //提取media.film.directors ，放入有序数组
        Map maps = new LinkedHashMap();
        String directorsString = media.getFilm().getDirectors();
        String[] directorsArray = directorsString.split(",");
        for (String directorDouBanNo : directorsArray) {
            maps.put(directorDouBanNo,starRepository.findByDouBanNo(directorDouBanNo));
        }
        model.addAttribute("directors",maps);

        maps = new LinkedHashMap();
        String actorsString = media.getFilm().getActors();
        String[] actorsArray = actorsString.split(",");
        for (String actorDouBanNo : actorsArray) {
            maps.put(actorDouBanNo,starRepository.findByDouBanNo(actorDouBanNo));
        }
        model.addAttribute("actors",maps);

        return "pages/info_film";
    }
}
