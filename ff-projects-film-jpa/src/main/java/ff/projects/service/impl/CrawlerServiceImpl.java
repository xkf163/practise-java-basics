package ff.projects.service.impl;

import ff.projects.common.ResultBean;
import ff.projects.crawler.DouBanProcessor;
import ff.projects.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @Author:xukangfeng
 * @Description
 * @Date : Create in 14:24 2017/11/1
 */
@Service
public class CrawlerServiceImpl implements CrawlerService {

    @Autowired
    DouBanProcessor douBanProcessor;

    @Override
    public Object[] running(String mutil, String singleFilmUrl, String thread, String homepage) {
        douBanProcessor.setSingleCrawler(true);
        douBanProcessor.newFilmList = new ArrayList<>();
        douBanProcessor.newPersonList = new ArrayList<>();
        if ("1".equals(mutil))
            douBanProcessor.setSingleCrawler(false);
        if ("1".equals(homepage))
            singleFilmUrl= "https://movie.douban.com/";
        Spider.create(douBanProcessor).addUrl(singleFilmUrl).thread(Integer.parseInt(thread)).run();
        return new Object[]{douBanProcessor.newFilmList,douBanProcessor.newPersonList};
    }
}
