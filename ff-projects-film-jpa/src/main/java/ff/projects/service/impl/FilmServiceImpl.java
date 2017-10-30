package ff.projects.service.impl;

import com.querydsl.core.types.Predicate;
import ff.projects.entity.Film;
import ff.projects.entity.QFilm;
import ff.projects.repository.FilmRepository;
import ff.projects.service.FilmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;

/**
 * Created by xukangfeng on 2017/10/28 13:00
 */
@Service
public class FilmServiceImpl implements FilmService {

    @Autowired
    FilmRepository filmRepository;

    @Override
    public Film refineFilmSubjectFromCrawler(Page page) {
        Film f = new Film();
        //影片页
        page.putField("subject", page.getHtml().xpath("//title/text()").regex("(.*)\\s*\\(豆瓣\\)"));
        f.setSubject(page.getResultItems().get("subject").toString());

//        if (page.getResultItems().get("name") == null) {
//            //skip this page
//            page.setSkip(true);
//        }

        //豆瓣编号
        f.setDoubanNo(page.getUrl().regex("/subject/(\\d+)/").toString());
//        if (page.getUrl().regex(URL_ENTITY).match()) {
//            f.setDoubanNo(page.getUrl().regex("https://movie\\.douban\\.com/subject/(\\d+)/\\?from=.*").toString());
//        } else {
//            f.setDoubanNo(page.getUrl().regex("https://movie\\.douban\\.com/subject/(\\d+)/").toString());
//        }
        return f;
    }

    @Override
    public Film refineFilmFromCrawler(Page page) {

        Film f = new Film();
        //影片页
        page.putField("subject", page.getHtml().xpath("//title/text()").regex("(.*)\\s*\\(豆瓣\\)"));
        f.setSubject(page.getResultItems().get("subject").toString());

        //豆瓣编号
        f.setDoubanNo(page.getUrl().regex("/subject/(\\d+)/").toString());
//        if (page.getUrl().regex(URL_ENTITY).match()) {
//            f.setDoubanNo(page.getUrl().regex("https://movie\\.douban\\.com/subject/(\\d+)/\\?from=.*").toString());
//        } else {
//            f.setDoubanNo(page.getUrl().regex("https://movie\\.douban\\.com/subject/(\\d+)/").toString());
//        }
        //影片简介
        Selectable selectableInfo = page.getHtml().xpath("//div[@id='info']");
        page.putField("info", selectableInfo);
        f.setInfo(page.getResultItems().get("info").toString());
        //imdb编号
        String imdbNo = selectableInfo.regex("<a href=\"http://www.imdb.com/title/tt\\d+\" target=\"_blank\" rel=\"nofollow\">(tt\\d+)</a>").toString();
        f.setImdbNo(imdbNo);
        //
        page.putField("subjectMain", page.getHtml().xpath("//div[@id='content']/h1//span[@property='v:itemreviewed']/text()"));
        f.setSubjectMain(page.getResultItems().get("subjectMain").toString());
        //年代
        page.putField("year", page.getHtml().xpath("//div[@id='content']/h1//span[@class='year']/text()").regex("\\((.*)\\)"));
        f.setYear(Short.parseShort(page.getResultItems().get("year").toString()));
//      page.putField("imdb", page.getHtml().xpath("//div[@id='info']").regex("<a href=\"http://www.imdb.com/title/tt\\d+\" target=\"_blank\" rel=\"nofollow\">(tt\\d+)</a>"));
        page.putField("introduce", page.getHtml().xpath("//div[@class='related-info']//div[@class='indent']//span[@property='v:summary']/text()"));
        f.setIntroduce(page.getResultItems().get("introduce").toString());
        //豆瓣评分及评分人数
        Selectable selectableRating = page.getHtml().xpath("//div[@typeof='v:Rating']");
        PlainText object = (PlainText) selectableRating.xpath("//strong/text()");
        if (null != object && !"".equals(object.getFirstSourceText())) {
            f.setDoubanRating(Float.parseFloat(selectableRating.xpath("//strong/text()").toString()));
            f.setDoubanSum(Long.parseLong(selectableRating.xpath("//span[@property='v:votes']/text()").toString()));
        }
        //导演
        f.setDirectors(StringUtils.join(selectableInfo.xpath("//a[@rel='v:directedBy']/@href").regex("/celebrity/(\\d+)/").all().toArray(), ","));
        //演员
        f.setActors(StringUtils.join(selectableInfo.xpath("//a[@rel='v:starring']/@href").regex("/celebrity/(\\d+)/").all().toArray(), ","));
        //影片类别
        f.setGenre(StringUtils.join(selectableInfo.xpath("//span[@property='v:genre']/text()").all().toArray(), ","));
        //发行日期
        f.setInitialReleaseDate(StringUtils.join(selectableInfo.xpath("//span[@property='v:initialReleaseDate']/text()").all().toArray(), ","));
        //影片时长
        f.setRuntime(StringUtils.join(selectableInfo.xpath("//span[@property='v:runtime']/@content").all().toArray(), ","));
        //影片所属国家/地区
        String country_temp = selectableInfo.regex("<span class=\"pl\">制片国家/地区:</span> (.*)\n" +
                " <br>").toString();
        if (null != country_temp && !"".equals(country_temp)) {
            String country = country_temp.substring(0, country_temp.indexOf("\n"));
            f.setCountry(country);
        }
        //影片其他片名
        String subject_temp = selectableInfo.regex("<span class=\"pl\">又名:</span> (.*)\n" +
                " <br>").toString();
        if (null != subject_temp && !"".equals(subject_temp)) {
            if (subject_temp.indexOf("\n") > 0) {
                String subjectOther = subject_temp.substring(0, subject_temp.indexOf("\n"));
                f.setSubjectOther(subjectOther);
            } else {
                f.setSubjectOther(subject_temp);
            }
        }

        //集数
        String episodeNumber = selectableInfo.regex("<span class=\"pl\">集数:</span> (.*)\n" +
                " <br>").toString();
        if (null != episodeNumber && !"".equals(episodeNumber)) {
            f.setEpisodeNumber(episodeNumber);
        }

        return f;
    }


    @Override
    public Film findBySubjectAndDoubanNo(Film film) {
        //判断数据库里是否存在
        QFilm qFilm = QFilm.film;
        Predicate predicate = qFilm.subject.eq(film.getSubject()).and(qFilm.doubanNo.eq(film.getDoubanNo()));
        return filmRepository.findOne(predicate);
    }

    @Override
    public void save(Film film) {
        filmRepository.save(film);
    }

}
