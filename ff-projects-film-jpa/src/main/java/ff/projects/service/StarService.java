package ff.projects.service;

import ff.projects.entity.Star;

import java.util.List;

/**
 * @Author:xukangfeng
 * @Description
 * @Date : Create in 8:48 2017/11/2
 */
public interface StarService {
    List<Star> findAll();

    Star findByDouBanNo(String douBanNo);
}
