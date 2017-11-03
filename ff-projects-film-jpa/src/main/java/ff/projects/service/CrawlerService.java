package ff.projects.service;

import ff.projects.common.ResultBean;

/**
 * @Author:xukangfeng
 * @Description
 * @Date : Create in 14:24 2017/11/1
 */
public interface CrawlerService {
    Object[] running(String mutil, String url, String thread, String homepage,String batchNumber);
}

