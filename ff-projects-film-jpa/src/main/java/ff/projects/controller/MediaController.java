package ff.projects.controller;

import ff.projects.entity.Media;
import ff.projects.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author:xukangfeng
 * @Description
 * @Date : Create in 16:26 2018/2/1
 */

@Controller
public class MediaController {

    @Autowired
    MediaRepository mediaRepository;
    /**
     *
     * @return
     */
    @GetMapping(value = "/media/{mid}")
    public String mediaShow(Model model, @PathVariable Long mid){
        Media media = mediaRepository.findOne(mid);
        model.addAttribute(media);
        return "pages/info_film";
    }
}
