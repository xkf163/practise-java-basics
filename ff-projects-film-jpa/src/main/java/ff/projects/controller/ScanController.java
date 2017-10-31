package ff.projects.controller;

import ff.projects.entity.Media;
import ff.projects.service.GatherService;
import ff.projects.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
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

    @GetMapping(value = "/scanning")
    @ResponseBody
    public List<Media> gather() {
        List<Media> lists = new ArrayList<>();

        File file = new File("J:/201610");
        lists= scanService.gatherMedia2DB(file);

        System.out.println("over。。。");
        return lists;
    }
}
