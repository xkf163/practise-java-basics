package ff.projects.controller;

import ff.projects.entity.Media;
import ff.projects.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.List;

/**
 * @Author:xukangfeng
 * @Description  扫描指定目录中的多媒体文件（夹）
 * @Date : Create in 15:56 2017/10/31
 */
@Controller
public class ScanController {

    @Autowired
    ScanService scanService;

    @PostMapping(value = "/scanning")
    @ResponseBody
    public List<Media> gather(@RequestParam(required = true) String parentFolder  ) {
        File file = new File(parentFolder);
        return scanService.gatherMedia2DB(file);

    }
}
