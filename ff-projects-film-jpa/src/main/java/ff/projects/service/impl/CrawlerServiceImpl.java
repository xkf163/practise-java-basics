package ff.projects.service.impl;

import ff.projects.crawler.DouBanProcessor;
import ff.projects.entity.Film;
import ff.projects.entity.Person;
import ff.projects.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

/**
 * @Author:xukangfeng
 * @Description
 * @Date : Create in 14:24 2017/11/1
 */
@Service
public class CrawlerServiceImpl implements CrawlerService {

    @Autowired
    DouBanProcessor douBanProcessor;

    @PersistenceContext
    EntityManager entityManager;

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

        //批量保存
        int size = douBanProcessor.newPersonList.size();
        for(int i = 0;i<size;i++){
            Person person = douBanProcessor.newPersonList.get(i);
            entityManager.persist(person);
            if(i % 50 == 0 || i==size-1){
                entityManager.flush();
                entityManager.clear();
            }

        }
        size = douBanProcessor.newFilmList.size();
        for (int i = 0;i<size;i++){
            Film film = douBanProcessor.newFilmList.get(i);
            entityManager.persist(film);
            if(i % 50 == 0 || i==size-1){
                entityManager.flush();
                entityManager.clear();
            }

        }



        return new Object[]{douBanProcessor.newFilmList,douBanProcessor.newPersonList};
    }
}
