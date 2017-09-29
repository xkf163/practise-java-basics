package ff.projects.crawler;

import com.querydsl.core.types.Predicate;
import ff.projects.entity.Film;
import ff.projects.entity.QFilm;
import ff.projects.repository.FilmRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;

/**
 * Created by F on 2017/6/29.
 */
@Service
@Data
public class DouBanPatchProcessor implements PageProcessor {


    @Autowired
    FilmRepository filmRepository;

    public static final String URL_ENTITY_SHORT= "https://movie\\.douban\\.com/subject/\\d+/";

    private Site site = Site
            .me()
            .setDomain("movie.douban.com")
            .setSleepTime(10000).setRetryTimes(3)
            .setCharset("utf-8")
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");


    @Override
    public void process(Page page) {

        if (page.getUrl().regex(URL_ENTITY_SHORT).match()) {
            //更新subjectOther、douban评分、评分人数
            //影片页
            String subjectMain = page.getHtml().xpath("//div[@id='content']/h1//span[@property='v:itemreviewed']/text()").toString();
            String doubanNo = page.getUrl().regex("https://movie\\.douban\\.com/subject/(\\d+)/").toString();
            //判断数据库里是否存在
            Film f = null;
            QFilm qFilm = QFilm.film;
            Predicate predicate = qFilm.doubanNo.eq(doubanNo);
            f = filmRepository.findOne(predicate);
            boolean changed = false;
            if (null != f) {
                //豆瓣评分
                Selectable selectableRating = page.getHtml().xpath("//div[@typeof='v:Rating']");
                PlainText object = (PlainText) selectableRating.xpath("//strong/text()");
                if(null != object && !"".equals(object.getFirstSourceText())){
                    f.setDoubanRating(Float.parseFloat(selectableRating.xpath("//strong/text()").toString()));
                    f.setDoubanSum(Long.parseLong(selectableRating.xpath("//span[@property='v:votes']/text()").toString()));
                    changed = true;
                }


                Selectable selectableInfo = page.getHtml().xpath("//div[@id='info']");

                String subject_temp = selectableInfo.regex("<span class=\"pl\">又名:</span> (.*)\n" +
                        " <br>").toString();
                if (null != subject_temp && !"".equals(subject_temp)) {
                    if(subject_temp.indexOf("\n")>0){
                        String subjectOther = subject_temp.substring(0, subject_temp.indexOf("\n"));
                        f.setSubjectOther(subjectOther);
                        System.out.println("1） subject_other name is : "+f.getSubjectOther());
                        changed = true;
                    }else {
                        f.setSubjectOther(subject_temp);
                        System.out.println("1） subject_other name is : "+f.getSubjectOther());
                        changed = true;
                    }
                }

                if(changed){
                    filmRepository.save(f);
                    System.out.println("2）update success for film named: "+subjectMain);
                }else{
                    System.out.println("!）nothing changed for film named: "+subjectMain);
                }

            }
        }
    }



    @Override
    public Site getSite() {
        return site;
    }
}



