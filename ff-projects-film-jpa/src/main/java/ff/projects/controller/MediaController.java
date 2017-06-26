package ff.projects.controller;

import ff.projects.entity.Media;
import ff.projects.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by F on 2017/6/21.
 */
@RestController
public class MediaController {

    @Autowired
    MediaRepository mediaRepository;

    @PostMapping(value = "/medias")
    public Page<Media> getAllMedia(@RequestParam(value = "page",defaultValue = "1",required = false) String page, @RequestParam(value = "rows",defaultValue = "10",required = false) String size){
        Pageable pageable = new PageRequest(Integer.parseInt(page)-1, Integer.parseInt(size), new Sort("name"));
        return  mediaRepository.findAll(pageable);
    }
}
