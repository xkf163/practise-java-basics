package ff.projects.controller;

import ff.projects.crawler.DouBanProcessor;
import ff.projects.entity.Media;
import ff.projects.entity.MediaVO;
import ff.projects.repository.MediaRepository;
import ff.projects.service.GatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import us.codecraft.webmagic.Spider;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by F on 2017/6/15.
 */
@Controller
public class IndexController {

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    GatherService gatherService;

    @Autowired
    DouBanProcessor douBanProcessor;

    @RequestMapping(method = RequestMethod.GET,value = "/")
    public String index(HttpServletRequest request){
        return "index";
    }


    @RequestMapping(method = RequestMethod.GET,value = "/pages/{path}")
    public String pageIndex(Model model,HttpServletRequest request,@PathVariable String path){
        String dataUrl = request.getParameter("dataUrl");
        model.addAttribute("dataUrl",dataUrl);
        return "pages/"+path;
    }


    @GetMapping(value = "/gather")
    @ResponseBody
    public List<Media> gather() {
        List<Media> lists = new ArrayList<>();
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int intRetVal = jfc.showDialog(new JLabel(), "选择根文件夹");
        File file = jfc.getSelectedFile();
        System.out.println(intRetVal);
        if( intRetVal == JFileChooser.APPROVE_OPTION){
            lists= gatherService.gatherMedia2DB(jfc.getSelectedFile());
        }
        System.out.println("over。。。");
        return lists;
    }

    @GetMapping(value = "/transfer")
    @ResponseBody
    public List<MediaVO> transfer(){
        return gatherService.Media2MediaVO();
    }


    @GetMapping(value = "/crawler")
    public String crawler(){
//        Spider.create(douBanProcessor).addUrl("https://movie.douban.com/subject/5308265/?from=aaa").thread(5).run();
        Spider.create(douBanProcessor).addUrl("https://movie.douban.com/").thread(5).run();
        return "redirect:/";
    }

    @GetMapping(value = "/pickup")
    public void pickUp(){
        gatherService.pickUp();

    }

}
