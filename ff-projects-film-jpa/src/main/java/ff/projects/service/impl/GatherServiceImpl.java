package ff.projects.service.impl;

import com.querydsl.core.types.Predicate;
import ff.projects.entity.*;
import ff.projects.repository.*;
import ff.projects.service.GatherService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by F on 2017/6/21.
 * 20160520
 * 从硬盘中收集原始电影文件（夹）数据，并写入Media表中供后续数据提取
 */
@Service
public class GatherServiceImpl implements GatherService {


    @Autowired
    MediaVORepository mediaVORepository;

    @Autowired
    MediaVOFilmVORepository mediaVOFilmVORepository;

    @Autowired
    FilmRepository filmRepository;


    /**
     * 提取并转换爬虫爬取的第一手数据
     * 转换成FilmVOPersonVO
     */
    @Override
    public Object[] connectFilmForMedia(){

        List<Film> successFilmList = new ArrayList<>();
        List<Film> failedFilmList = new ArrayList<>();

        QMediaVO qMediaVO = QMediaVO.mediaVO;
        List<MediaVO> mediaVOList = (List<MediaVO>) mediaVORepository.findAll(qMediaVO.media.deleted.eq(0));
        QFilm qFilm = QFilm.film;
        Predicate[] predicateArray = new Predicate[6];
        int i=0;
        for(MediaVO mediaVO : mediaVOList){
            //mediaVO = mediaVORepository.findOne((long) 957);
            i++;
            //1: subject精确匹配
            predicateArray[0]=qFilm.subject.trim().eq(mediaVO.getNameChn().trim()).and(qFilm.year.eq(mediaVO.getYear()));
            //2:
            predicateArray[1]=qFilm.subject.trim().eq(mediaVO.getNameChn().trim()).and(qFilm.subjectMain.trim().contains(mediaVO.getNameEng().trim())).and(qFilm.year.eq(mediaVO.getYear()));
            //3:
            predicateArray[2]=qFilm.subject.trim().eq(mediaVO.getNameChn().trim()).and(qFilm.subjectOther.trim().contains(mediaVO.getNameEng().trim())).and(qFilm.year.eq(mediaVO.getYear()));

            //predicateArray[3]=qFilm.subject.trim().notEqualsIgnoreCase(mediaVO.getNameChn().trim()).and(qFilm.subjectMain.trim().contains(mediaVO.getNameChn().trim())).and(qFilm.subjectMain.trim().contains(mediaVO.getNameEng().trim())).and(qFilm.year.eq(mediaVO.getYear()));
            //3: 0
            predicateArray[3]=qFilm.subject.trim().notEqualsIgnoreCase(mediaVO.getNameChn().trim()).and(qFilm.subjectMain.trim().contains(mediaVO.getNameEng().trim()).and(qFilm.subjectOther.contains(mediaVO.getNameChn().trim()))).and(qFilm.year.eq(mediaVO.getYear()));
            //2: 2>1
            predicateArray[4]=qFilm.subject.trim().notEqualsIgnoreCase(mediaVO.getNameChn().trim()).and(qFilm.subjectOther.trim().contains(mediaVO.getNameEng().trim()).and(qFilm.subjectOther.contains(mediaVO.getNameChn().trim()))).and(qFilm.year.eq(mediaVO.getYear()));

            predicateArray[5]=qFilm.subject.trim().notEqualsIgnoreCase(mediaVO.getNameChn().trim()).and(qFilm.subjectOther.trim().notEqualsIgnoreCase(mediaVO.getNameEng().trim()).and(qFilm.subjectOther.contains(mediaVO.getNameChn().trim()))).and(qFilm.year.eq(mediaVO.getYear()));

            for (int j = 0; j<predicateArray.length; j++){
                Film filmVO = null;
                MediaVOFilmVO mf = null;
                if(j>4){
                    System.out.println(j);
                }
                List<Film> filmList = (List<Film>) filmRepository.findAll(predicateArray[j]);
                if(filmList.size() == 1){
                    filmVO = filmList.get(0);
                    System.out.println(i+": "+mediaVO.getMedia().getName()+" -> "+filmVO.getSubjectMain());
                    mf = new MediaVOFilmVO();
                    mf.setFilmVOId(filmVO.getId());
                    mf.setMediaVOId(mediaVO.getId());
                    mediaVOFilmVORepository.save(mf);
                    break;
                }
            }
        }

        return new Object[]{successFilmList,failedFilmList};
    }



    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonVOFilmVORepository personVOFilmVORepository;

    /**
     * 人物和电影对应关系创建
     */
    @Override
    public void relation(){

        QMediaVO qMediaVO = QMediaVO.mediaVO;
        List<MediaVO> mediaVOList = (List<MediaVO>) mediaVORepository.findAll(qMediaVO.media.deleted.eq(0));

        QFilm qFilm = QFilm.film;
        QPerson qPerson = QPerson.person;
        QPersonVOFilmVO qPersonVOFilmVO = QPersonVOFilmVO.personVOFilmVO;
        QMediaVOFilmVO qMediaVOFilmVO = QMediaVOFilmVO.mediaVOFilmVO;



        int i=0;
        for(MediaVO mediaVO : mediaVOList){
            Film filmVO = null;
            System.out.println("mediaVO.getId(): "+mediaVO.getId());
            Predicate predicate1 = qMediaVOFilmVO.mediaVOId.eq(mediaVO.getId());
            MediaVOFilmVO qMediaVOFilmVO1 = mediaVOFilmVORepository.findOne(predicate1);
            if(null == qMediaVOFilmVO1){
                continue;
            }

            Predicate predicate = qFilm.id.eq(qMediaVOFilmVO1.getFilmVOId());
            List<Film> filmList = (List<Film>) filmRepository.findAll(predicate);
            if(filmList.size() == 1){
                i++;
                filmVO = filmList.get(0);
                System.out.println(i+": "+mediaVO.getMedia().getName()+" -> "+filmVO.getSubjectMain());

                String filmId = String.valueOf(filmVO.getId());
                String asD = filmVO.getDirectors();
                String asA = filmVO.getActors();

                if(!"".equals(asA)){
                    String[] asAA = asA.split(",");
                    for (String personDoubanNo :asAA){

                        Predicate predicateP =  qPerson.doubanNo.eq(personDoubanNo);
                        Person person = personRepository.findOne(predicateP);

                        if(null != person){
                            PersonVOFilmVO personVOFilmVO = null;
                            Predicate predicateA = qPersonVOFilmVO.person.eq(person);
                            List<PersonVOFilmVO> personVOFilmVOList = (List<PersonVOFilmVO>) personVOFilmVORepository.findAll(predicateA);
                            if(personVOFilmVOList.size() == 0){
                                personVOFilmVO = new PersonVOFilmVO();
                                personVOFilmVO.setPerson(person);
                                personVOFilmVO.setAsActor(filmId);
                                personVOFilmVO.setAsActorNumber(1);
                            }else if(personVOFilmVOList.size() == 1){
                                personVOFilmVO = personVOFilmVOList.get(0);
                                String dFilmIds = personVOFilmVO.getAsActor();
                                if(null!=dFilmIds){
                                    String[] dFilmIds_array = dFilmIds.split(",");
                                    if(!Arrays.asList(dFilmIds_array).contains(filmId)){

                                        List<String> strings = new ArrayList<>();
                                        for (String string : dFilmIds_array){
                                            strings.add(string);
                                        }
                                        strings.add(filmId);
                                        dFilmIds_array = strings.toArray(new String[strings.size()]);
                                        personVOFilmVO.setAsActorNumber(strings.size());
                                    }
                                    personVOFilmVO.setAsActor(StringUtils.join(dFilmIds_array,","));
                                }else {
                                    personVOFilmVO.setAsActor(filmId);
                                    personVOFilmVO.setAsActorNumber(1);
                                }
                            }
                            personVOFilmVORepository.save(personVOFilmVO);
                        }
                    }


                }

                if(!"".equals(asD)){

                    String[] asDA = asD.split(",");
                    for (String personDoubanNo :asDA){

                        Predicate predicateP =  qPerson.doubanNo.eq(personDoubanNo);
                        Person person = personRepository.findOne(predicateP);


                        if(null != person){
                            PersonVOFilmVO personVOFilmVO = null;
                            Predicate predicateA = qPersonVOFilmVO.person.eq(person);
                            List<PersonVOFilmVO> personVOFilmVOList = (List<PersonVOFilmVO>) personVOFilmVORepository.findAll(predicateA);
                            if(personVOFilmVOList.size() == 0){
                                personVOFilmVO = new PersonVOFilmVO();
                                personVOFilmVO.setPerson(person);
                                personVOFilmVO.setAsDirector(filmId);
                                personVOFilmVO.setAsDirectorNumber(1);
                            }else if(personVOFilmVOList.size() == 1){
                                personVOFilmVO = personVOFilmVOList.get(0);
                                String dFilmIds = personVOFilmVO.getAsDirector();
                                if(null!=dFilmIds){
                                    String[] dFilmIds_array = dFilmIds.split(",");
                                    if(!Arrays.asList(dFilmIds_array).contains(filmId)){

                                        List<String> strings = new ArrayList<>();
                                        for (String string : dFilmIds_array){
                                            strings.add(string);
                                        }
                                        strings.add(filmId);
                                        dFilmIds_array = strings.toArray(new String[strings.size()]);
                                        personVOFilmVO.setAsDirectorNumber(strings.size());
                                    }
                                    personVOFilmVO.setAsDirector(StringUtils.join(dFilmIds_array,","));
                                }else {
                                    personVOFilmVO.setAsDirector(filmId);
                                    personVOFilmVO.setAsDirectorNumber(1);
                                }
                            }
                            personVOFilmVORepository.save(personVOFilmVO);
                        }


                    }

                }

            }
        }


        System.out.println("ending....."+i);

    }

    /**
     *
     * @param personId
     * @return
     */
    @Override
     public  List<Film> listFilmsByPersonId(String personId, String type){
//        personId ="9112";

         PersonVOFilmVO personVOFilmVO1 = personVOFilmVORepository.findOne(Long.parseLong(personId));
         String films = personVOFilmVO1.getAsDirector();
         if("2".equals(type)){
             films = personVOFilmVO1.getAsActor();
         }
         String[] filmArray = films.split(",");

         QFilm qFilm = QFilm.film;
         Predicate predicate1 = qFilm.id.stringValue().in(Arrays.asList(filmArray));
         return (List<Film>) filmRepository.findAll(predicate1,new Sort("year"));
     }


}
