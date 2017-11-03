package ff.projects.service.impl;

import ff.projects.crawler.DouBanProcessor;
import ff.projects.entity.Film;
import ff.projects.entity.Person;
import ff.projects.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public Object[] running(String mutil, String singleFilmUrl, String thread, String homepage,String batchNumber) {

        //初始化doubanprocess数据
        douBanProcessor.setSingleCrawler(true);

        douBanProcessor.savedFilms=new ArrayList<>();
        douBanProcessor.savedPersons=new ArrayList<>();

        douBanProcessor.needSaveFilms=new ArrayList<>();
        douBanProcessor.needSavePersons=new ArrayList<>();
        //批量保存临界值
        douBanProcessor.setBatchNumber(Integer.parseInt(batchNumber));
        if ("1".equals(mutil))
            douBanProcessor.setSingleCrawler(false);
        if ("1".equals(homepage))
            singleFilmUrl = "https://movie.douban.com/";
        Spider.create(douBanProcessor).addUrl(singleFilmUrl).thread(Integer.parseInt(thread)).run();

        //保存剩余数据
        for(int i = 0;i<douBanProcessor.needSavePersons.size();i++){
            Person person = douBanProcessor.needSavePersons.get(i);
            entityManager.persist(person);
        }
        for (int i = 0;i<douBanProcessor.needSaveFilms.size();i++){
            Film film = douBanProcessor.needSaveFilms.get(i);
            entityManager.persist(film);
        }
        entityManager.flush();
        entityManager.clear();
        douBanProcessor.savedPersons.addAll(douBanProcessor.needSavePersons);
        douBanProcessor.savedFilms.addAll(douBanProcessor.needSaveFilms);

        //返回前端数据
        return new Object[]{douBanProcessor.savedFilms,douBanProcessor.savedPersons};
    }

}
