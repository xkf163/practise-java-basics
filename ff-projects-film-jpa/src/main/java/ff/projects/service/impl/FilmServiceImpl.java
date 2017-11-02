package ff.projects.service.impl;

import com.querydsl.core.types.Predicate;
import ff.projects.entity.*;
import ff.projects.repository.FilmRepository;
import ff.projects.repository.MediaRepository;
import ff.projects.repository.MediaVOFilmVORepository;
import ff.projects.service.FilmService;
import ff.projects.service.PersonService;
import ff.projects.service.StarService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Created by xukangfeng on 2017/10/28 13:00
 */
@Service
public class FilmServiceImpl implements FilmService {

    @Autowired
    StarService starService;

    @Autowired
    PersonService personService;

    @Autowired
    FilmRepository filmRepository;


    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Film extractFilmFirstFromCrawler(Page page) {
        Film f = new Film();
        //影片页
        //1）片名
        page.putField("subject", page.getHtml().xpath("//title/text()").regex("(.*)\\s*\\(豆瓣\\)"));
        f.setSubject(page.getResultItems().get("subject").toString());


        Selectable selectableInfo = page.getHtml().xpath("//div[@id='info']");
        //2）导演
        f.setDirectors(StringUtils.join(selectableInfo.xpath("//a[@rel='v:directedBy']/@href").regex("/celebrity/(\\d+)/").all().toArray(), ","));
        //3）演员
        f.setActors(StringUtils.join(selectableInfo.xpath("//a[@rel='v:starring']/@href").regex("/celebrity/(\\d+)/").all().toArray(), ","));

        //4）豆瓣编号
        f.setDoubanNo(page.getUrl().regex("/subject/(\\d+)/").toString());
        //5、6）豆瓣评分及评分人数
        Selectable selectableRating = page.getHtml().xpath("//div[@typeof='v:Rating']");
        PlainText object = (PlainText) selectableRating.xpath("//strong/text()");
        if (null != object && !"".equals(object.getFirstSourceText())) {
            f.setDoubanRating(Float.parseFloat(selectableRating.xpath("//strong/text()").toString()));
            f.setDoubanSum(Long.parseLong(selectableRating.xpath("//span[@property='v:votes']/text()").toString()));
        }
        //7)集数
        String episodeNumber = selectableInfo.regex("<span class=\"pl\">集数:</span> (.*)\n" +
                " <br>").toString();
        if (null != episodeNumber && !"".equals(episodeNumber)) {
            f.setEpisodeNumber(episodeNumber);
        }

        return f;
    }

    @Override
    public Film extractFilmSecondFromCrawler(Page page,Film f) {

        //影片页
        //片名
        //page.putField("subject", page.getHtml().xpath("//title/text()").regex("(.*)\\s*\\(豆瓣\\)"));
        //f.setSubject(page.getResultItems().get("subject").toString().trim());

        //豆瓣编号
        //f.setDoubanNo(page.getUrl().regex("/subject/(\\d+)/").toString());

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
//        Selectable selectableRating = page.getHtml().xpath("//div[@typeof='v:Rating']");
//        PlainText object = (PlainText) selectableRating.xpath("//strong/text()");
//        if (null != object && !"".equals(object.getFirstSourceText())) {
//            f.setDoubanRating(Float.parseFloat(selectableRating.xpath("//strong/text()").toString()));
//            f.setDoubanSum(Long.parseLong(selectableRating.xpath("//span[@property='v:votes']/text()").toString()));
//        }
        //导演
        //f.setDirectors(StringUtils.join(selectableInfo.xpath("//a[@rel='v:directedBy']/@href").regex("/celebrity/(\\d+)/").all().toArray(), ","));
        //演员
        //f.setActors(StringUtils.join(selectableInfo.xpath("//a[@rel='v:starring']/@href").regex("/celebrity/(\\d+)/").all().toArray(), ","));
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

    @Override
    public boolean needCrawler(Film f) {
        //几种不需要保存的条件：
        //1）导演和主演列表为空就skip,不保存
        //2）发现集数不为空，判断是电视剧，
        //3）暂无评分 也不保存
        if (StringUtils.isBlank(f.getActors()) || StringUtils.isBlank(f.getDirectors()) || f.getEpisodeNumber()!=null || f.getDoubanRating()==null){
            //skip this page
            return false;
        }
        //4)判断数据库里是否存在
        Film film = findBySubjectAndDoubanNo(f);
        if (null != film){
            return false;
        }
        return true;
    }




    /**
     * 提取并转换爬虫爬取的第一手数据
     * 转换成FilmVOPersonVO
     */
    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    MediaVOFilmVORepository mediaVOFilmVORepository;

    @Override
    @Transactional
    public Object[] connectFilmForMedia(){

        List<Media> notFindMediaList = new ArrayList<>();
        List<Media> needUpdateMediaList = new ArrayList<>();
        List<Star> needUpdateStarList = new ArrayList<>();
        List<Star> needSaveStarList = new ArrayList<>();

        Map<String,Star> starHashMapInit = new HashMap<>();
        Map<String,Star> starHashMapFinal = new HashMap<>();
        List<Star> starsList = starService.findAll();
        //遍历库中stars，组成map，，后续根据doubanid来判断库中是否已存在
        for (Star star : starsList){
            starHashMapInit.put(star.getDouBanNo(),star);
            starHashMapFinal=starHashMapInit;
        }



        QMedia qMedia = QMedia.media;
        List<Media> mediaList = (List<Media>) mediaRepository.findAll(qMedia.deleted.eq(0));

        for(Media media : mediaList){
            boolean thisMediaNeedUpdate = false;
            Film film = findConnectedFilmForMedia(media);
            if(film==null){
                notFindMediaList.add(media);
                continue;
            }

            //-----当前正在处理的filmId
            String filmId = String.valueOf(film.getId());


            //step2)保存或更新当前film中的stars
            String directorsDoubanNo = film.getDirectors();
            String actorsDoubanNo = film.getActors();
            //director doubanid array
            String[] ddno_array = directorsDoubanNo.split(",");
            String[] adno_array = actorsDoubanNo.split(",");
            //as导演
            for(String ddno : ddno_array){
                //找star表，看是否存在，不存在则新建，存在即asdirect加上此filmid（先判断有无此filmid）
                if(starHashMapFinal.containsKey(ddno)){
                    Star star = starHashMapFinal.get(ddno);
                    //判断当前filmid是否已存在当前star的asdirect字段中
                    //不存在add进去，并更新number
                    String[] asDArray = star.getAsDirector().split(",");
                    if(!Arrays.asList(asDArray).contains(filmId)){
                        String[] asDArrayNew = new String[asDArray.length+1];
                        System.arraycopy(asDArray, 0, asDArrayNew, 0, asDArray.length);//将a数组内容复制新数组b
                        asDArrayNew[asDArrayNew.length-1]=filmId;
                        star.setAsDirector(StringUtils.join(asDArrayNew,","));
                        star.setAsDirectorNumber(asDArrayNew.length);
                        //此star一开始就没有，已经在needsaveStarlist中，否则会重复添加重复生成
                        if(starHashMapInit.containsKey(star.getDouBanNo())){
                            needUpdateStarList.add(star);
                        }

                        thisMediaNeedUpdate=true;
                    }
                }else{
                    //new
                    Star star = new Star();
                    star.setDouBanNo(ddno);
                    star.setAsDirectorNumber(1);
                    star.setAsDirector(filmId);
                    star.setPersonId(personService.findByDoubanNo(ddno).getId());
                    needSaveStarList.add(star);
                    //加入到starlist，防止重复生成star数据
                    starHashMapFinal.put(star.getDouBanNo(),star);
                    thisMediaNeedUpdate=true;
                }


            }
            //as主演
            for(String adno : adno_array){
                if(starHashMapFinal.containsKey(adno)){

                }else{
                    //new
                }
            }

            //step1)update media obj
            if(thisMediaNeedUpdate){
                media.setFilmId(film.getId());
                media.setUpdateDate(new Date());
                needUpdateMediaList.add(media);
            }


        }


        /*
         批量保存
        List<Media> needUpdateMediaList = new ArrayList<>();
        List<Star> needUpdateStarList = new ArrayList<>();
        List<Star> needSaveStarList = new ArrayList<>();
         */
        int size  = needUpdateMediaList.size();
        System.out.println("needUpdateMediaList:::"+size);
        for (int i=0; i<size; i++){
            Media media = needUpdateMediaList.get(i);
            entityManager.merge(media);
            if(i % 50 == 0 || i==size-1){
                    entityManager.flush();
                    entityManager.clear();
            }
        }
        size  = needUpdateStarList.size();
        System.out.println("needUpdateStarList:::"+size);
        for (int i=0; i<size; i++){
            Star media = needUpdateStarList.get(i);
            entityManager.merge(media);
            if(i % 50 == 0 || i==size-1){
                entityManager.flush();
                entityManager.clear();
            }
        }
        size  = needSaveStarList.size();
        System.out.println("needSaveStarList:::"+size);
        for (int i=0; i<size; i++){
            Star media = needSaveStarList.get(i);
            entityManager.persist(media);
            if(i % 50 == 0 || i==size-1){
                entityManager.flush();
                entityManager.clear();
            }
        }




        return new Object[]{needUpdateMediaList,notFindMediaList,needUpdateStarList,needSaveStarList};
    }


    /**
     * 根据media条目信息去film表找一一对应的filmobj，找不到返回null
     * @param media
     * @return
     */
    Film findConnectedFilmForMedia(Media media){
        if(media.getFilmId()!=null){
            return filmRepository.findOne(media.getFilmId());
        }

        QFilm qFilm = QFilm.film;
        Predicate[] predicateArray = new Predicate[6];
        //1: subject精确匹配
        predicateArray[0]=qFilm.subject.trim().eq(media.getNameChn().trim()).and(qFilm.year.eq(media.getYear()));
        //2:
        predicateArray[1]=qFilm.subject.trim().eq(media.getNameChn().trim()).and(qFilm.subjectMain.trim().contains(media.getNameEng().trim())).and(qFilm.year.eq(media.getYear()));
        //3:
        predicateArray[2]=qFilm.subject.trim().eq(media.getNameChn().trim()).and(qFilm.subjectOther.trim().contains(media.getNameEng().trim())).and(qFilm.year.eq(media.getYear()));

        //predicateArray[3]=qFilm.subject.trim().notEqualsIgnoreCase(mediaVO.getNameChn().trim()).and(qFilm.subjectMain.trim().contains(mediaVO.getNameChn().trim())).and(qFilm.subjectMain.trim().contains(mediaVO.getNameEng().trim())).and(qFilm.year.eq(mediaVO.getYear()));
        //3: 0
        predicateArray[3]=qFilm.subject.trim().notEqualsIgnoreCase(media.getNameChn().trim()).and(qFilm.subjectMain.trim().contains(media.getNameEng().trim()).and(qFilm.subjectOther.contains(media.getNameChn().trim()))).and(qFilm.year.eq(media.getYear()));
        //2: 2>1
        predicateArray[4]=qFilm.subject.trim().notEqualsIgnoreCase(media.getNameChn().trim()).and(qFilm.subjectOther.trim().contains(media.getNameEng().trim()).and(qFilm.subjectOther.contains(media.getNameChn().trim()))).and(qFilm.year.eq(media.getYear()));

        predicateArray[5]=qFilm.subject.trim().notEqualsIgnoreCase(media.getNameChn().trim()).and(qFilm.subjectOther.trim().notEqualsIgnoreCase(media.getNameEng().trim()).and(qFilm.subjectOther.contains(media.getNameChn().trim()))).and(qFilm.year.eq(media.getYear()));
        for (int j = 0; j<predicateArray.length; j++){
            List<Film> filmList = (List<Film>) filmRepository.findAll(predicateArray[j]);
            if(filmList.size() == 1){
                return filmList.get(0);
            }
        }

        return null;

    }

}
