package ff.projects.service.impl;

import com.querydsl.core.types.Predicate;
import ff.projects.entity.Person;
import ff.projects.entity.QPerson;
import ff.projects.repository.PersonRepository;
import ff.projects.service.PersonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

/**
 * @Author:xukangfeng
 * @Description
 * @Date : Create in 10:34 2017/10/30
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepository personRepository;

    @Override
    public Person refinePersonFromCrawler(Page page) {

        Person p = new Person();
        //姓名
        page.putField("name", page.getHtml().xpath("//title/text()").regex("(.*)\\s*\\(豆瓣\\)"));
        p.setName(page.getResultItems().get("name").toString());
        //豆瓣编号
        p.setDoubanNo(page.getUrl().regex("https://movie\\.douban\\.com/celebrity/(\\d+)/").toString());

        //人物脚本信息
        page.putField("nameExtend", page.getHtml().xpath("//div[@id='content']/h1/text()"));
        page.putField("introduce", page.getHtml().xpath("//div[@id='intro']//div[@class='bd']//span[@class='all hidden']/text()"));

        //人物简介字符串集合
        Selectable selectableInfo =  page.getHtml().xpath("//div[@class='article']//div[@class='info']//li");
        page.putField("info", selectableInfo);

        String gender = StringUtils.join( selectableInfo.regex("<li> <span>性别</span>: (.*) </li>").all().toArray(),",");
        String birthday = StringUtils.join( selectableInfo.regex("<li> <span>出生日期</span>: (.*) </li>").all().toArray(),",");
        String birthplace = StringUtils.join( selectableInfo.regex("<li> <span>出生地</span>: (.*) </li>").all().toArray(),",");
        String profession = StringUtils.join( selectableInfo.regex("<li> <span>职业</span>: (.*) </li>").all().toArray(),",");
        String imdbNo = StringUtils.join( selectableInfo.regex("<li> <span>imdb编号</span>: (.*) </li>").regex("<a href=\"http://www.imdb.com/name/nm\\d+\" target=\"_blank\">(nm\\d+)</a>").all().toArray(),",");

        p.setNameExtend(page.getResultItems().get("nameExtend").toString());
        p.setIntroduce(page.getResultItems().get("introduce").toString());
        p.setInfo(page.getResultItems().get("info").toString());

        p.setBirthDay(birthday);
        p.setJob(profession);
        p.setGender(gender);
        p.setImdbNo(imdbNo);
        p.setBirthPlace(birthplace);

        return p;
    }

    @Override
    public Person refinePersonNameFromCrawler(Page page) {
        Person p = new Person();
        page.putField("name", page.getHtml().xpath("//title/text()").regex("(.*)\\s*\\(豆瓣\\)"));
        p.setName(page.getResultItems().get("name").toString());
        p.setDoubanNo(page.getUrl().regex("https://movie\\.douban\\.com/celebrity/(\\d+)/").toString());
        return p;
    }

    @Override
    public Person findByNameAndDoubanNO(Person p) {
        QPerson qPerson = QPerson.person;
        Predicate predicate = qPerson.name.eq(p.getName()).and(qPerson.doubanNo.eq(p.getDoubanNo()));
        return personRepository.findOne(predicate);
    }

    @Override
    public void save(Person person) {

    }
}
