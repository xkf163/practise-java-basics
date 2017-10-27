package ff.projects.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ff.projects.common.TreeNode;
import ff.projects.entity.QMediaVO;
import ff.projects.service.TreeService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TreeServiceImpl implements TreeService {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public TreeNode getTreeGroupByGatherDate() {
        //树1：收集年月------------s------------------
        TreeNode pTreeNode,treeNode = null;
        Set<TreeNode> treeNodeSet =  new LinkedHashSet<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        QMediaVO mediaVO = QMediaVO.mediaVO;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        List<Date> gatherDateList = queryFactory
                .selectFrom(mediaVO)
                .select(mediaVO.media.gatherDate)
                .fetch();
        //去重
        List<Date> gatherDateListNew = new ArrayList<>(new HashSet<Date>(gatherDateList));
        Set<String> gatherDateSet = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if(Integer.parseInt(o1) < Integer.parseInt(o2)){
                    return 1;
                }else if(Integer.parseInt(o1) == Integer.parseInt(o2)) {
                    return 0;
                }
                return -1;
            }
        });

        for (Date date : gatherDateListNew){
            gatherDateSet.add(sdf.format(date));
        }

        //准备展开后的树
        Iterator<String> iterator = gatherDateSet.iterator();
        while (iterator.hasNext()){
            String yearMonth = iterator.next();
            treeNode = new TreeNode(yearMonth, "/pages/table_mediaVO?toolbarType=toolbar_queryWithoutDate&dataUrl=/mediavo/gatherdate/"+yearMonth,"/mediavo/gatherdate/"+yearMonth);
            treeNodeSet.add(treeNode);
        }
        pTreeNode = new TreeNode("收集年月", "closed", "/pages/table_mediaVO?dataUrl=/mediavo/gatherdates", "/mediavo/gatherdates" , treeNodeSet);
        return pTreeNode;
    }

    @Override
    public TreeNode getTreeGroupByMovieReleaseYear() {
        TreeNode pTreeNode,treeNode = null;
        Set<TreeNode> treeNodeSet =  new LinkedHashSet<>();
        QMediaVO mediaVO = QMediaVO.mediaVO;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        //树2：影片年代------------s------------------
        List<Short>  list = queryFactory
                .selectFrom(mediaVO)
                .select(mediaVO.year)
                .distinct()
                .orderBy(mediaVO.year.desc())
                .fetch();
        treeNodeSet = new LinkedHashSet<>();
        for(Short year : list){
            treeNode = new TreeNode(String.valueOf(year), "/pages/table_mediaVO?dataUrl=/mediavo/years/"+String.valueOf(year),"/mediavo/years/"+String.valueOf(year));
            treeNodeSet.add(treeNode);
        }
        pTreeNode = new TreeNode("影片年代", "closed", "/pages/table_mediaVO?dataUrl=/mediavo/years", "/mediavo/years", treeNodeSet);

        return pTreeNode;
    }
}
