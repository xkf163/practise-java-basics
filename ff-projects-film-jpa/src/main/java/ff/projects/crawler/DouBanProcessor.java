package ff.projects.crawler;

import ff.projects.entity.Film;
import ff.projects.entity.Person;
import ff.projects.service.FilmService;
import ff.projects.service.PersonService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by F on 2017/7/1.
 * 豆瓣电影及人物信息爬虫工具类
 */
@Service
@Data
public class DouBanProcessor implements PageProcessor {

    public static final String URL_FILM = "/subject/\\d+/";
//  https://movie.douban.com/subject/1291839/?from=subject-page
    public static final String URL_FILM_FROM_SUBJECT_PAGE=  "/subject/\\d+/\\?from=subject-page";
    //豆瓣首页抓取用
    public static final String URL_FILM_FROM_SHOWING=  "/subject/\\d+/\\?from=showing";
    //https://movie.douban.com/subject/24753477/?tag=%E7%83%AD%E9%97%A8&from=gaia
    public static final String URL_FILM_FROM_HOT=  "/subject/\\d+/\\?tag=.*&from=.*";
    public static final String URL_PERSON= "/celebrity/\\d+/";
    public static final String URL_PERSON_FULL= "https://movie\\.douban\\.com/celebrity/\\d+/";
    public static final String URL_HOMEPAGE= "https://movie\\.douban\\.com";
    public static final String URL_SEARCH = "https://movie\\.douban\\.com/subject_search";


    public static List<Film> savedFilms; //此次任务最终完成的数据，返回给前端
    public static List<Person> savedPersons;
    public static List<Film> needSaveFilms;  //临时数据，每次批量保存后清空
    public static List<Person> needSavePersons; //临时数据，每次批量保存后清空
    public static List<String> dbPersonsDouBanNo; //数据库已存persons的doubanno
    public static List<String> dbFilmsDouBanNo; //数据库已存persons的doubanno


    @Autowired
    FilmService filmService;

    @Autowired
    PersonService personService;

    @PersistenceContext
    EntityManager entityManager;

    //爬虫是否单个电影爬取，默认单个爬取完成后就结束；false即无限延伸爬取，时间比较长
    public boolean singleCrawler = true;
    //批量保存临界个数
    public int batchNumber = 10 ;

    private Site site = Site
            .me()
            .setDomain("movie.douban.com")
            //%7B%22distinct_id%22%3A%20%2215cf2586637111-09cc812eb1fc81-701238-2a3000-15cf258663828b%22%2C%22%24id%22%3A%20%22163054123%22%2C%22initial_view_time%22%3A%20%221498714502%22%2C%22initial_referrer%22%3A%20%22https%3A%2F%2Fmovie.douban.com%2Fsubject_search%3Fsearch_text%3D%25E7%2581%25AB%25E9%2594%2585%25E8%258B%25B1%25E9%259B%2584%26cat%3D1002%22%2C%22initial_referrer_domain%22%3A%20%22movie.douban.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201498717202%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201498717202%7D;
            // UM_distinctid=15cf2586637111-09cc812eb1fc81-701238-2a3000-15cf258663828b; ap=1; ue="xkf99@qq.com"; dbcl2="163054123:chmaQ1NsVJU"; ck=t0ID; _pk_id.100001.4cf6=a90e45eb3a458ed3.1498541166.30.1498897875.1498881693.; _pk_ses.100001.4cf6=*; __utma=30149280.561785331.1498541167.1498880150.1498896335.30;
            // __utmb=30149280.0.10.1498896335; __utmc=30149280; __utmz=30149280.1498541167.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmv=30149280.16305; __utma=223695111.754876944.1498541167.1498880150.1498896335.30; __utmb=223695111.0.10.1498896335; __utmc=223695111; __utmz=223695111.1498541167.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); push_noty_num=0; push_doumail_num=0; _vwo_uuid_v2=D8570F0119A0D26A171627FC7CD214FE|f9fd83098aac087eb826bd151219de18
            .addCookie("ll","118172")
            .addCookie("bid","tPZ_SBQRBFY")
            .addCookie("__yadk_uid","HhhHmaMNwKkDGee1buMnf6U6X5Ej1FQB")
            .addCookie("CNZZDATA1258025570","427171256-1498714502-https%253A%252F%252Fmovie.douban.com%252F%7C1498714502")
            .addCookie("cn_d6168da03fa1ahcc4e86_dplus","%7B%22distinct_id%22%3A%20%2215cf2586637111-09cc812eb1fc81-701238-2a3000-15cf258663828b%22%2C%22%24id%22%3A%20%22163054123%22%2C%22initial_view_time%22%3A%20%221498714502%22%2C%22initial_referrer%22%3A%20%22https%3A%2F%2Fmovie.douban.com%2Fsubject_search%3Fsearch_text%3D%25E7%2581%25AB%25E9%2594%2585%25E8%258B%25B1%25E9%259B%2584%26cat%3D1002%22%2C%22initial_referrer_domain%22%3A%20%22movie.douban.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201498717202%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201498717202%7D")
            .addCookie("UM_distinctid","15cf2586637111-09cc812eb1fc81-701238-2a3000-15cf258663828b")
            .addCookie("ap","1")
            .addCookie("ps","y")
            .addCookie("_pk_ref.100001.4cf6","%5B%22%22%2C%22%22%2C1509717014%2C%22https%3A%2F%2Fwww.douban.com%2Flogin%3Fredir%3Dhttps%253A%252F%252Fmovie.douban.com%252F%22%5D")
            .addCookie("ue","xkf99@qq.com")
            .addCookie("dbcl2","163054123:OPWsXSJ6OhU")
            .addCookie("__utmt","1")
            .addCookie("ck","rlyv")
            .addCookie("_pk_id.100001.4cf6","a90e45eb3a458ed3.1498541166.43.1509720903.1509709842.")
            .addCookie("_pk_ses.100001.4cf6","*")
            .addCookie("__utma","30149280.561785331.1498541167.1509709790.1509717014.43")
            .addCookie("__utmb","30149280.21.10.1509717014")
            .addCookie("__utmc","30149280")
            .addCookie("__utmz","30149280.1498924164.32.2.utmcsr=localhost:81|utmccn=(referral)|utmcmd=referral|utmcct=/persons/373/films/type/2")
            .addCookie("__utmv","30149280.16305")
            .addCookie("__utma","223695111.754876944.1498541167.1509709790.1509717014.43")
            .addCookie("__utmb","223695111.0.10.1509717014")
            .addCookie("__utmc","223695111")
            .addCookie("__utmz","223695111.1509636191.41.4.utmcsr=douban.com|utmccn=(referral)|utmcmd=referral|utmcct=/login")
            .addCookie("push_noty_num","0")
            .addCookie("push_doumail_num","0")
            .addCookie("_vwo_uuid_v2","D8570F0119A0D26A171627FC7CD214FE|f9fd83098aac087eb826bd151219de18")

//            .addCookie("viewed","3135476")
//            .addCookie("gr_user_id","922cd5e7-60dc-4640-92ac-f15bf31d7a41")
//            .addCookie("as","https://movie.douban.com/")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
            .addHeader("Accept-Encoding", "gzip, deflate, br")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
            .addHeader("Connection", "keep-alive")
            .addHeader("Cache-Control", "max-age=0")
            .setSleepTime(10000).setRetryTimes(3)
            .setCharset("utf-8")
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36");
           // .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");

    @Override
    @Transactional
    public void process(Page page) {

        //电影页面
        if (page.getUrl().regex(URL_FILM).match() ) {


            //从网页中提取filmObject，只是部分字段，用于判断是否需要保存此object。
            Film f = filmService.extractFilmFirstFromCrawler(page);
            if (filmService.needCrawler(f)) {
                //完整提取film信息
                f = filmService.extractFilmSecondFromCrawler(page,f);
                //filmService.save(f);
                needSaveFilms.add(f);

            }else{
                //skip this page
                page.setSkip(true);
            }

            //判断是否是电影，电视剧跳过执行
            //从页面发现后续的url地址来抓取:不管film存不存在，都把人物url加入到任务列表中，避免：某些情况film成功抓取后，人物抓取时由于超时或其他错误造成抓取失败，抓取失败后就永远不会再进行抓取了
            //1)当前电影所有人物的url
            if(true){
                Selectable selectable = page.getHtml().css("div.subject.clearfix").links().regex(URL_PERSON);
                List<String> personListFinally = filterUrl(selectable,"/celebrity/(\\d+)/",dbPersonsDouBanNo);
                page.addTargetRequests(personListFinally);

            }

            //不管当前film是否已存在，还是把关联film是加入到任务队列，尽可能扩大范围
            if(!this.singleCrawler){
                //2）后续的电影url，有10个
                //2.1)取出后续电影doubannNo LIST，判断dbFilmsDouBanNoList是否已存在，已存在就不add了
                Selectable selectable = page.getHtml().xpath("//div[@class='recommendations-bd']/dl/dt").links().regex(URL_FILM_FROM_SUBJECT_PAGE);
                List<String> filmListFinally = filterUrl(selectable,"/subject/(\\d+)/",dbFilmsDouBanNo);
                page.addTargetRequests(filmListFinally);

            }




        }else if(page.getUrl().regex(URL_PERSON).match()){

            Person p = personService.extractFilmFirstFromCrawler(page);
            if(personService.needCrawler(p)){
                //完整提取person信息
                p = personService.extractFilmSecondFromCrawler(page);
                //personService.save(p);
                needSavePersons.add(p);

            }else {
                page.setSkip(true);
            }

            //扩大爬虫范围，不管当前person是否已经存在
            if(!this.singleCrawler){
                //最受欢迎5部
                //page.addTargetRequests(page.getHtml().xpath("//div[@id='best_movies']").css("div.info").links().regex(URL_FILM).all());
                Selectable selectable = page.getHtml().xpath("//div[@id='best_movies']").css("div.info").links().regex(URL_FILM);
                List<String> filmListFinally = filterUrl(selectable,"/subject/(\\d+)/",dbFilmsDouBanNo);
                page.addTargetRequests(filmListFinally);
                //合作2次以上的影人
                //page.addTargetRequests(page.getHtml().xpath("//div[@id='partners']").css("div.pic").links().regex(URL_PERSON).all());
            }


        } else if(page.getUrl().regex(URL_SEARCH).match()){
            //关键字搜索URL
           // https://movie.douban.com/subject_search?search_text=%E9%BE%99
            //搜索结果第一页
            //*[@id="root"]/div/div[2]/div[1]/div[1]/div[13]/div/div[1]
            //page.addTargetRequests(page.getHtml().xpath("//*[@class=\"title\"]").links().regex(URL_FILM).all());

            Selectable selectable = page.getHtml().links().regex(URL_FILM);
            List<String> filmListFinally = filterUrl(selectable,"/subject/(\\d+)/",dbFilmsDouBanNo);
            page.addTargetRequests(filmListFinally);

        }else if(page.getUrl().regex(URL_HOMEPAGE).match()){
            //入口是豆瓣主页
            //1)正在热映
            //page.addTargetRequests(page.getHtml().xpath("//*[@id=\"screening\"]/div[2]/ul/li/ul/li[2]").links().regex(URL_FILM_FROM_SHOWING).all());

            Selectable selectable = page.getHtml().xpath("//*[@id=\"screening\"]/div[2]/ul/li/ul/li[2]").links().regex(URL_FILM_FROM_SHOWING);
            List<String> filmListFinally = filterUrl(selectable,"/subject/(\\d+)/",dbFilmsDouBanNo);
            page.addTargetRequests(filmListFinally);

            //2)最近热门电影:貌似是动态生成，抓不到
            //page.addTargetRequests(page.getHtml().xpath("//div[@class='slide-page']").links().regex(URL_FILM_FROM_HOT).all());
        } else {
//            错误：URL不符合规则,直接退出
        }


        /**
         * 批量保存，而不是抓一个就保存一次
         */
        System.out.println("-->NeedSavePersons::::"+needSavePersons.size());
        System.out.println("-->NeedSaveFilms::::::"+needSaveFilms.size());
        int size = needSavePersons.size()+needSaveFilms.size();
        if(size >= batchNumber){
            try {
                for(int i = 0;i<needSavePersons.size();i++){
                    Person person = needSavePersons.get(i);
                    //存在隐患：万一保存失败
                    dbPersonsDouBanNo.add(person.getDoubanNo());
                    entityManager.persist(person);
                }
                for (int i = 0;i<needSaveFilms.size();i++){
                    Film film = needSaveFilms.get(i);
                    //存在隐患：万一保存失败
                    dbFilmsDouBanNo.add(film.getDoubanNo());
                    entityManager.persist(film);
                }
                entityManager.flush();
                entityManager.clear();
            }catch (Exception e){
                needSavePersons.clear();
                needSaveFilms.clear();
            }finally {
                //加入到savedPersons
                savedPersons.addAll(needSavePersons);
                needSavePersons.clear();
                savedFilms.addAll(needSaveFilms);
                needSaveFilms.clear();
            }
        }
    }



    public List<String> filterUrl(Selectable selectable,String regexRule,List<String> dbList){
        //原始urls
        List<String> oriList =selectable.all();
        List<String> oriDouBanNoList =selectable.regex(regexRule).all();
        List<String> finallyList = new ArrayList<>(oriList);
        int i;
        for (i=0;i <oriDouBanNoList.size(); i++){
            if(dbList.contains(oriDouBanNoList.get(i))){
                //数据库已存在，不加入抓取队列
                finallyList.remove(oriList.get(i));
            }
        }
        return finallyList;
    }


    @Override
    public Site getSite() {
        return site;
    }
}
