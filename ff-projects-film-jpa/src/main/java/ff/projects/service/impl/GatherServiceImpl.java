package ff.projects.service.impl;

import com.querydsl.core.types.Predicate;
import ff.projects.entity.Media;
import ff.projects.entity.MediaVO;
import ff.projects.entity.QMedia;
import ff.projects.entity.QMediaVO;
import ff.projects.repository.MediaRepository;
import ff.projects.repository.MediaVORepository;
import ff.projects.service.GatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by F on 2017/6/21.
 * 20160520
 * 从硬盘中收集原始电影文件（夹）数据，并写入Media表中供后续数据提取
 */
@Service
public class GatherServiceImpl implements GatherService {

    private String[] folderKeys  = new String[] {"720P","1080P","DTS","CMCT","TLF","CHD","HDS","HDSKY","FRDS","EPIC","HDAREA","ATOMS","TRUEHD","X264","X265","DVDRIP"};
    private String[] filePostfix = new String[]{"MKV","TS","AVI"};
    private List<Media> mediaEntries;
    private List<Media> mediaEntriesTotal;
    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    MediaVORepository mediaVORepository;

    List<Media> oldMediaList;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<MediaVO> Media2MediaVO() {
        QMedia qMedia = QMedia.media;
        Predicate predicate = qMedia.whetherTransfer.eq(1);
        List<Media> medias =(List<Media>) mediaRepository.findAll(predicate);
        List<MediaVO> mediaVOS = new ArrayList<MediaVO>();
        Pattern pattern;
        Matcher matcher;
        String year="";
        String mediaName;
        Boolean finded;
        for (Media media : medias){
            finded = false;
            MediaVO mediaVO = new MediaVO();
            mediaName = media.getName();
            System.out.println(mediaName);
//            mediaName="2001太空漫游.1968.A.Space.Odyssey.720p.MNHD-FRDS";
            //正则表达式提取年代
            pattern = Pattern.compile("([1-2][9|0][0-9]{2})(\\.[B|H])");
            matcher = pattern.matcher(mediaName);
            if (matcher.find()){
                year = matcher.group(1);
                finded = true;
            }else{
                pattern = Pattern.compile("([1-2][9|0][0-9]{2})(\\.)");
                matcher = pattern.matcher(mediaName);
                if (matcher.find()){finded = true;year = matcher.group(1);}
            }

            mediaVO.setMedia(media);
            mediaVO.setYear(Short.parseShort(year));
            mediaVO.setNameChn(mediaName.substring(0,mediaName.indexOf(".")));
            if(finded && mediaName.indexOf(year)-1>mediaName.indexOf(".")+1)
                mediaVO.setNameEng(mediaName.substring(mediaName.indexOf(".")+1,mediaName.indexOf(year)-1).replaceAll("\\."," "));

            mediaVORepository.save(mediaVO);
            mediaVOS.add(mediaVO);

            media.setWhetherTransfer(0);
            mediaRepository.save(media);

            System.out.println(mediaVO);
        }

        System.out.println("end ......");
        return mediaVOS;
    }

    @Override
    @Transactional
    @Modifying
    public List<Media> gatherMedia2DB(File fileDir) {
        mediaEntriesTotal = new ArrayList<>();
        mediaEntriesTotal.addAll(gatherMedia2DBing(fileDir));
        return mediaEntriesTotal;
    }


    /**
     * 收集medias主方法
     * @param fileDir
     * @return
     */
    public List<Media> gatherMedia2DBing(File fileDir) {

        mediaEntries = new ArrayList<>();

        /*
         * step1:获取选择的路径：如j:\201702 或者 j:\;拆分出盘符及文件夹
         */
        String rootFullPath = fileDir.getAbsolutePath();
        String rootDisk = rootFullPath.substring(0,rootFullPath.indexOf(":"));
        String rootFolder =  rootFullPath.substring(rootFullPath.indexOf(":")+1,rootFullPath.length());

        /*
         * step2: 根据这两个关键字去搜索数据库现有的条目，并把deleted删除标记位赋值为1，即认为是删除的
         */
        QMedia media = QMedia.media;
        QMediaVO mediaVO = QMediaVO.mediaVO;
        Predicate predicate = media.diskNo.eq(rootDisk).and(media.fullPath.like(rootFolder+"%")).and(media.deleted.eq(0));
        oldMediaList = (List<Media>) mediaRepository.findAll(predicate);

//        JPAUpdateClause jpaUpdateClause = new JPAUpdateClause(entityManager,media);
//
//        long s = jpaUpdateClause.where(predicate).set(media.deleted,Boolean.TRUE).execute();
//        entityManager.flush();
//        System.out.println("mediaList.size()："+mediaList.size()+" execute: "+s);

        File[] mediaFiles = fileDir.listFiles();
        System.out.println(fileDir+" 该目录文件数量:"+mediaFiles.length);
        for(File f:mediaFiles){
            if( f.isDirectory() && !f.isHidden()){
                if(folderFilter(f.getName())){
                    System.out.println("是多媒体文件夹："+f.getName());
                    deal(f,true);
                }else{
                    System.out.println("不是多媒体文件夹："+f.getName());
                    mediaEntriesTotal.addAll(gatherMedia2DBing(f));
                }
            }else if(f.isFile()){
                if( fileFilter(f.getName()) && !f.isHidden() ){
                    System.out.println("是文件");
                    deal(f,false);
                }
            }
        }

        /*
          *  step3: 剩余oldmediaList里的数据都是失效，deleted标记1
         */
        System.out.println("oldmedialist: "+oldMediaList.size());
        for (Media media1 : oldMediaList){
            media1.setDeleted(1);
            media1.setUpdateDate(new Date());
            mediaRepository.save(media1);
        }
        return mediaEntries;
    }


    public void deal(File f,boolean folder) {
        String fullPath = f.getAbsolutePath();

        FileTime fileTime= null;
        try {
            fileTime = Files.readAttributes(Paths.get(fullPath), BasicFileAttributes.class).creationTime();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Calendar cd = Calendar.getInstance();
        Media oldMedia = whetherExist(fullPath.substring(fullPath.indexOf(":")+1,fullPath.length()));
        if(null == oldMedia ){
            Media m = new Media();
                m.setFullPath(fullPath.substring(fullPath.indexOf(":")+1,fullPath.length()));
                m.setDiskNo(fullPath.substring(0,fullPath.indexOf(":")));
                m.setName(fullPath.lastIndexOf("\\")>0?fullPath.substring(fullPath.lastIndexOf("\\")+1,fullPath.length()):fullPath);
                m.setDiskSize(calcFolderSize(f));

                m.setMediaSize(calc(calcFolderSize(f)));
                //f的最后修改时间
                cd.setTimeInMillis(f.lastModified());
                m.setModifiedDate(cd.getTime());
                //f的创建（下载）资源时间
                cd.setTimeInMillis(fileTime.toMillis());
                m.setGatherDate(cd.getTime());

                m.setCreateDate(new Date());
                m.setWhetherFolder(folder?1:0);
                m.setWhetherAlive(1);
                m.setWhetherTransfer(1);
                mediaRepository.save(m);
                mediaEntries.add(m);

            System.out.println("------insert----------"+m.getFullPath());

        }else{

            System.out.println("------update----------"+oldMedia.getFullPath());
            oldMediaList.remove(oldMedia);
            oldMedia.setId(oldMedia.getId());
            oldMedia.setUpdateDate(new Date());
            mediaRepository.save(oldMedia);
        }
    }



    /**
     * 判断数据库中是否已经存在相同全路径的条目
     * @param fullPath
     * @return
     */
    public Media whetherExist(String fullPath){


        QMedia qMedia = QMedia.media;
        Predicate predicate = qMedia.deleted.eq(0).and(qMedia.fullPath.eq(fullPath));
        List<Media> list = (List<Media>) mediaRepository.findAll(predicate);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }



    /**
	 * 计算文件（夹）大小，以字节为单位
	 */
    public long calcFolderSize(File file){
        if(file.exists()){
            if(file.isDirectory()){
                long tol = 0;
                File[] fs = file.listFiles();
                for(File f:fs){
                    tol+=calcFolderSize(f);
                }
                return tol;
            }else{
                return	file.length();
            }
        }
        return 0;
    }

    /**
     * 转化字节大小为MB、GB单位
     * @param size
     * @return
     */
    public String calc(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

    /**
     * 判断文件夹是否是存放多媒体文件夹
     */
    public boolean folderFilter(String folderName){
        boolean flag = false;
        folderName=folderName.toUpperCase();
        for(String k:folderKeys){
            if	(folderName.indexOf(k)>0) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /*
 * 判断文件是否是多媒体文件，根据后缀来匹配
 */
    public boolean fileFilter(String fileName){
        boolean flag = false;
        String[] fileNameArr = fileName.toUpperCase().split("\\.");
        String CurPostfix = fileNameArr[fileNameArr.length-1];
        for(String k:filePostfix){
            if	(CurPostfix.indexOf(k)>-1) {
                flag = true;
                break;
            }
        }
        return flag;

    }


}
