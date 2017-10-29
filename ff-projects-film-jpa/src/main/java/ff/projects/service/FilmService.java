package ff.projects.service;

import ff.projects.entity.Film;
import us.codecraft.webmagic.Page;

/**
 * Created by xukangfeng on 2017/10/28 12:59
 */
public interface FilmService {

    //从网页中提取Film Object
    Film refineFilmFromCrawler(Page page);

    Film refineFilmSubjectFromCrawler(Page page);

    Film findBySubjectAndDoubanNo(Film film);

    void save(Film film);
}
