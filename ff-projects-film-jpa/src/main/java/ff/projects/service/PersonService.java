package ff.projects.service;

import ff.projects.entity.Person;
import us.codecraft.webmagic.Page;

/**
 * @Author:xukangfeng
 * @Description
 * @Date : Create in 10:33 2017/10/30
 */
public interface PersonService {

    //从网页中提取Film Object
    Person refinePersonFromCrawler(Page page);

    Person refinePersonNameFromCrawler(Page page);

    Person findByNameAndDoubanNO(Person person);

    void save(Person person);

}
