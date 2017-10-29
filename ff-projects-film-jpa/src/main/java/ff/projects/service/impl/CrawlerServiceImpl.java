package ff.projects.service.impl;

import com.querydsl.core.types.Predicate;
import ff.projects.entity.Film;
import ff.projects.entity.QFilm;
import ff.projects.service.CrawlerServcie;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;

/**
 * Created by xukangfeng on 2017/10/28 13:03
 */
@Service
public class CrawlerServiceImpl implements CrawlerServcie {

    @Override
    public Page refinePage(Page page) {
        return null;
    }
}
