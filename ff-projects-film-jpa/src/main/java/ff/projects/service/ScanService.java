package ff.projects.service;

import ff.projects.entity.Media;

import java.io.File;
import java.util.List;

/**
 * @Author:xukangfeng
 * @Description
 * @Date : Create in 15:57 2017/10/31
 */
public interface ScanService {
    Object[] gatherMedia2DB(File fileDir);
}
