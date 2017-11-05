package ff.projects.service.impl;

import ff.projects.common.CrawlerCMD;
import ff.projects.crawler.DouBanProcessor;
import ff.projects.entity.Film;
import ff.projects.entity.Person;
import ff.projects.service.CrawlerService;
import ff.projects.service.FilmService;
import ff.projects.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Spider;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:xukangfeng
 * @Description
 * @Date : Create in 14:24 2017/11/1
 */
@Service
public class CrawlerServiceImpl implements CrawlerService {

    @Autowired
    DouBanProcessor douBanProcessor;

    @Autowired
    FilmService filmService;

    @Autowired
    PersonService personService;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public Object[] running(String mutil, String singleFilmUrl, String thread, String homepage,String batchNumber,String keySearch,String onePage) {


        //初始化doubanprocess数据
        douBanProcessor.setSingleCrawler(true);

        douBanProcessor.savedFilms=new ArrayList<>();
        douBanProcessor.savedPersons=new ArrayList<>();

        douBanProcessor.needSaveFilms=new ArrayList<>();
        douBanProcessor.needSavePersons=new ArrayList<>();

        douBanProcessor.dbFilmsDouBanNo  = filmService.listFilmsDouBanNo();
        douBanProcessor.dbPersonsDouBanNo = personService.listPersonsDouBanNo();

        //批量保存临界值
        douBanProcessor.setBatchNumber(Integer.parseInt(batchNumber));
        //延伸爬取
        if ("1".equals(mutil))
            douBanProcessor.setSingleCrawler(false);
        //首页进入
        if ("1".equals(homepage)) {
            singleFilmUrl = "https://movie.douban.com/";
            Spider.create(douBanProcessor).addUrl(singleFilmUrl).thread(Integer.parseInt(thread)).run();
        }else if ("1".equals(keySearch)) {
            //根据片名组装成搜索页面urls：url格式https://movie.douban.com/subject_search?search_text=%E9%BE%99
            String[] searchUrls = new String[]{"1", "2"};
            List<String> listFilmsName = filmService.listFilmsNameUnconnect();
            List<String> listSearchUrl = new ArrayList<>();
            for(String filmName:listFilmsName){
                listSearchUrl.add("https://movie.douban.com/subject_search?search_text="+filmName+"&cat=1002");
            }
            Spider.create(douBanProcessor).addUrl(listSearchUrl.toArray(new String[listSearchUrl.size()])).thread(Integer.parseInt(thread)).run();
        }else{

            //默认spider
            Spider spider = Spider.create(douBanProcessor).addUrl(singleFilmUrl).thread(Integer.parseInt(thread));
            CrawlerCMD.globalSpider = spider;
            spider.run();
        }

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
