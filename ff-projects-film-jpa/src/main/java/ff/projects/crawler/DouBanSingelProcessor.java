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
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;

/**
 * Created by F on 2017/7/1.
 */
@Service
@Data
public class DouBanSingelProcessor  implements PageProcessor {


    public static final String URL_ENTITY= "https://movie\\.douban\\.com/subject/\\d+/\\?from=.*";

    public static final String URL_ENTITY_SHORT= "https://movie\\.douban\\.com/subject/\\d+/";

    public static final String URL_ENTER= "https://movie\\.douban\\.com/";
    //celebrity/1022721/
    public static final String URL_PERSON= "/celebrity/\\d+/";

    public static final String URL_PERSON_FULL= "https://movie\\.douban\\.com/celebrity/\\d+/";

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    PersonRepository personRepository;

    private Site site = Site
            .me()
            .setDomain("movie.douban.com")
            //%7B%22distinct_id%22%3A%20%2215cf2586637111-09cc812eb1fc81-701238-2a3000-15cf258663828b%22%2C%22%24id%22%3A%20%22163054123%22%2C%22initial_view_time%22%3A%20%221498714502%22%2C%22initial_referrer%22%3A%20%22https%3A%2F%2Fmovie.douban.com%2Fsubject_search%3Fsearch_text%3D%25E7%2581%25AB%25E9%2594%2585%25E8%258B%25B1%25E9%259B%2584%26cat%3D1002%22%2C%22initial_referrer_domain%22%3A%20%22movie.douban.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201498717202%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201498717202%7D;
            // UM_distinctid=15cf2586637111-09cc812eb1fc81-701238-2a3000-15cf258663828b; ap=1; ue="xkf99@qq.com"; dbcl2="163054123:chmaQ1NsVJU"; ck=t0ID; _pk_id.100001.4cf6=a90e45eb3a458ed3.1498541166.30.1498897875.1498881693.; _pk_ses.100001.4cf6=*; __utma=30149280.561785331.1498541167.1498880150.1498896335.30;
            // __utmb=30149280.0.10.1498896335; __utmc=30149280; __utmz=30149280.1498541167.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmv=30149280.16305; __utma=223695111.754876944.1498541167.1498880150.1498896335.30; __utmb=223695111.0.10.1498896335; __utmc=223695111; __utmz=223695111.1498541167.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); push_noty_num=0; push_doumail_num=0; _vwo_uuid_v2=D8570F0119A0D26A171627FC7CD214FE|f9fd83098aac087eb826bd151219de18
            .addCookie("CNZZDATA1258025570","427171256-1498714502-https%253A%252F%252Fmovie.douban.com%252F%7C1498714502")
            .addCookie("__yadk_uid","XW2KSzLfC1NNzBidnF0W0DDZ6LkeuLOW")
            .addCookie("_pk_id.100001.4cf6","a90e45eb3a458ed3.1498541166.30.1498897875.1498881693.")
            .addCookie("_pk_ses.100001.4cf6","*")
            .addCookie("ps","y")
            .addCookie("UM_distinctid","15cf2586637111-09cc812eb1fc81-701238-2a3000-15cf258663828b")
            .addCookie("__utma","30149280.561785331.1498541167.1498880150.1498896335.30")
            .addCookie("__utmb","30149280.0.10.1498896335")
            .addCookie("__utmc","30149280")
            .addCookie("__utmv","30149280.16305")
            .addCookie("__utmz","30149280.1498541167.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)")
            .addCookie("_vwo_uuid_v2","D8570F0119A0D26A171627FC7CD214FE|f9fd83098aac087eb826bd151219de18")
            .addCookie("ap","1")
            .addCookie("bid","tPZ_SBQRBFY")
            .addCookie("cn_d6168da03fa1ahcc4e86_dplus","%7B%22distinct_id%22%3A%20%2215cf2586637111-09cc812eb1fc81-701238-2a3000-15cf258663828b%22%2C%22%24id%22%3A%20%22163054123%22%2C%22initial_view_time%22%3A%20%221498714502%22%2C%22initial_referrer%22%3A%20%22https%3A%2F%2Fmovie.douban.com%2Fsubject_search%3Fsearch_text%3D%25E7%2581%25AB%25E9%2594%2585%25E8%258B%25B1%25E9%259B%2584%26cat%3D1002%22%2C%22initial_referrer_domain%22%3A%20%22movie.douban.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201498717202%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201498717202%7D")
            .addCookie("dbcl2","163054123:chmaQ1NsVJU")
            .addCookie("ck","t0ID")
            .addCookie("ue","xkf99@qq.com")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
            .addHeader("Accept-Encoding", "gzip, deflate, br")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
            .addHeader("Connection", "keep-alive")
            .addHeader("Referer", "http://movie.douban.com/")
            .setSleepTime(10000).setRetryTimes(3)
            .setCharset("utf-8")
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36");


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
            //page.addTargetRequests(page.getHtml().links().regex(URL_PERSON_FULL).all());

        } else {
            //列表页
            //page.addTargetRequests(page.getHtml().xpath("//div[@id='screening']").links().regex(URL_ENTITY_A).all());
            page.addTargetRequests(page.getHtml().links().regex(URL_ENTITY_SHORT).all());

        }

    }

    @Override
    public Site getSite() {
        return site;
    }
}