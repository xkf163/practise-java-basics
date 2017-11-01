package ff.projects.restcontroller;

import ff.projects.common.ResultBean;
import ff.projects.entity.Media;
import ff.projects.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * @Author:xukangfeng
 * @Description  扫描指定目录中的多媒体文件（夹）
 * @Date : Create in 15:56 2017/10/31
 */
@RestController
public class ScanController {

    @Autowired
    ScanService scanService;

    @PostMapping(value = "/scanning")
    public ResultBean<Object[]> scanner(@RequestParam(required = true) String parentFolder) {
        return new ResultBean<Object[]>(scanService.gatherMedia2DB(new File(parentFolder)));
    }
}
