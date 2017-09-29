package ff.projects.crawler;

import com.querydsl.core.types.Predicate;
import ff.projects.entity.Film;
import ff.projects.entity.Person;
import ff.projects.entity.QFilm;
import ff.projects.entity.QPerson;
import ff.projects.repository.FilmRepository;
import ff.projects.repository.PersonRepository;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;

/**
 * Created by F on 2017/6/27.
 */
@Service
@Data
public class DouBanProcessor implements PageProcessor {

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    PersonRepository personRepository;



    public static final String URL_TOP= "https://movie\\.douban\\.com/top250\\?start=\\d+\\&filter=";

    public static final String URL_ENTITY= "https://movie\\.douban\\.com/subject/\\d+/\\?from=.*";

    public static final String URL_ENTITY_SHORT= "https://movie\\.douban\\.com/subject/\\d+/";

    public static final String URL_ENTER= "https://movie\\.douban\\.com/";

    //celebrity/1022721/
    public static final String URL_PERSON= "/celebrity/\\d+/";

    public static final String URL_PERSON_FULL= "https://movie\\.douban\\.com/celebrity/\\d+/";

    private Site site = Site
            .me()
            .setDomain("movie.douban.com")
            .setSleepTime(10000).setRetryTimes(3)
            .setCharset("utf-8")
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");



    @Override
    public void process(Page page) {

        if (page.getUrl().regex(URL_ENTITY).match()  || page.getUrl().regex(URL_ENTITY_SHORT).match() ) {
            Film f = new Film();
            //影片页
            page.putField("subject", page.getHtml().xpath("//title/text()").regex("(.*)\\s*\\(豆瓣\\)"));
            f.setSubject(page.getResultItems().get("subject").toString());

            if(page.getUrl().regex(URL_ENTITY).match()){
                f.setDoubanNo(page.getUrl().regex("https://movie\\.douban\\.com/subject/(\\d+)/\\?from=.*").toString());
            }else{
                f.setDoubanNo(page.getUrl().regex("https://movie\\.douban\\.com/subject/(\\d+)/").toString());
            }

            Selectable selectableInfo =page.getHtml().xpath("//div[@id='info']") ;
            String imdbNo = selectableInfo.regex("<a href=\"http://www.imdb.com/title/tt\\d+\" target=\"_blank\" rel=\"nofollow\">(tt\\d+)</a>").toString();
            f.setImdbNo(imdbNo);

            //判断数据库里是否存在
            Film film = null;
            QFilm qFilm = QFilm.film;
            Predicate predicate = qFilm.subject.eq(f.getSubject()).and(qFilm.doubanNo.eq(f.getDoubanNo()));
            film = filmRepository.findOne(predicate);

            if (null == film) {

                page.putField("subjectMain", page.getHtml().xpath("//div[@id='content']/h1//span[@property='v:itemreviewed']/text()"));
                page.putField("year", page.getHtml().xpath("//div[@id='content']/h1//span[@class='year']/text()").regex("\\((.*)\\)"));
//            page.putField("imdb", page.getHtml().xpath("//div[@id='info']").regex("<a href=\"http://www.imdb.com/title/tt\\d+\" target=\"_blank\" rel=\"nofollow\">(tt\\d+)</a>"));
                page.putField("introduce", page.getHtml().xpath("//div[@class='related-info']//div[@class='indent']//span[@property='v:summary']/text()"));
                page.putField("info", selectableInfo);
                f.setSubjectMain(page.getResultItems().get("subjectMain").toString());
                f.setYear(Short.parseShort(page.getResultItems().get("year").toString()));
                f.setInfo(page.getResultItems().get("info").toString());
                f.setIntroduce(page.getResultItems().get("introduce").toString());

                //豆瓣评分
                Selectable selectableRating =page.getHtml().xpath("//div[@typeof='v:Rating']") ;
                PlainText object = (PlainText) selectableRating.xpath("//strong/text()");
                if(null != object && !"".equals(object.getFirstSourceText())){
                    f.setDoubanRating(Float.parseFloat(selectableRating.xpath("//strong/text()").toString()));
                    f.setDoubanSum(Long.parseLong(selectableRating.xpath("//span[@property='v:votes']/text()").toString()));
                }



                f.setDirectors(StringUtils.join(selectableInfo.xpath("//a[@rel='v:directedBy']/@href").regex("/celebrity/(\\d+)/").all().toArray(),","));
                f.setActors(StringUtils.join(selectableInfo.xpath("//a[@rel='v:starring']/@href").regex("/celebrity/(\\d+)/").all().toArray(),","));
                f.setGenre(StringUtils.join(selectableInfo.xpath("//span[@property='v:genre']/text()").all().toArray(),","));
                f.setInitialReleaseDate(StringUtils.join(selectableInfo.xpath("//span[@property='v:initialReleaseDate']/text()").all().toArray(),","));
                f.setRuntime(StringUtils.join(selectableInfo.xpath("//span[@property='v:runtime']/@content").all().toArray(),","));


                String country_temp = selectableInfo.regex("<span class=\"pl\">制片国家/地区:</span> (.*)\n" +
                        " <br>").toString();
                if(null != country_temp && !"".equals(country_temp)){
                    String country = country_temp.substring(0,country_temp.indexOf("\n"));
                    f.setCountry(country);
                }


                String subject_temp = selectableInfo.regex("<span class=\"pl\">又名:</span> (.*)\n" +
                        " <br>").toString();
                if (null != subject_temp && !"".equals(subject_temp)) {
                    if(subject_temp.indexOf("\n")>0){
                        String subjectOther = subject_temp.substring(0, subject_temp.indexOf("\n"));
                        f.setSubjectOther(subjectOther);
                    }else {
                        f.setSubjectOther(subject_temp);
                    }
                }


                filmRepository.save(f);
            }
            page.addTargetRequests(page.getHtml().links().regex(URL_ENTITY_SHORT).all());
            page.addTargetRequests(page.getHtml().links().regex(URL_ENTITY).all());
            page.addTargetRequests(page.getHtml().links().regex(URL_PERSON).all());


        }else if(page.getUrl().regex(URL_PERSON_FULL).match()){
            Person p = new Person();
            page.putField("name", page.getHtml().xpath("//title/text()").regex("(.*)\\s*\\(豆瓣\\)"));
            p.setName(page.getResultItems().get("name").toString());
            p.setDoubanNo(page.getUrl().regex("https://movie\\.douban\\.com/celebrity/(\\d+)/").toString());
            //判断数据库里是否存在
            Person person = null;
            QPerson qPerson = QPerson.person;
            Predicate predicate = qPerson.name.eq(p.getName()).and(qPerson.doubanNo.eq(p.getDoubanNo()));
            person = personRepository.findOne(predicate);
            if (null == person) {
                //人物页
                page.putField("nameExtend", page.getHtml().xpath("//div[@id='content']/h1/text()"));
                page.putField("introduce", page.getHtml().xpath("//div[@id='intro']//div[@class='bd']//span[@class='all hidden']/text()"));

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

                personRepository.save(p);
            }
            page.addTargetRequests(page.getHtml().links().regex(URL_PERSON_FULL).all());

        } else {
            //列表页
            //page.addTargetRequests(page.getHtml().xpath("//div[@id='screening']").links().regex(URL_ENTITY_A).all());
            page.addTargetRequests(page.getHtml().links().regex(URL_TOP).all());
            page.addTargetRequests(page.getHtml().links().regex(URL_ENTITY_SHORT).all());

        }

    }

    @Override
    public Site getSite() {
        return site;
    }


//    public static void main(String[] args) { Spider.create(new DouBanProcessor()).addUrl("https://movie.douban.com/").thread(5).run(); }

    public static void main(String[] args) { Spider.create(new DouBanProcessor()).addUrl("https://movie.douban.com/subject/26667056").run(); }

}
