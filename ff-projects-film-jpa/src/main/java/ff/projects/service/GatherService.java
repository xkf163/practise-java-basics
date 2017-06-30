package ff.projects.service;

import ff.projects.entity.Media;
import ff.projects.entity.MediaVO;

import java.io.File;
import java.util.List;

/**
 * Created by F on 2017/6/21.
 */
public interface GatherService {

     List<Media> gatherMedia2DB(File fileDir);

     List<MediaVO> Media2MediaVO();

     void pickUp();

     void relation();

}
