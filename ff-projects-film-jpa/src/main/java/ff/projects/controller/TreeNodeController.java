package ff.projects.controller;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ff.projects.common.TreeNode;
import ff.projects.entity.QMediaVO;
import ff.projects.repository.MediaVORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by F on 2017/6/23.
 */
@RestController
public class TreeNodeController {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    MediaVORepository mediaVORepository;

    @GetMapping(value = "/tree")
    public List<TreeNode> listTree(){
        List<TreeNode> treeNodeList = new ArrayList<>();
        Set<TreeNode> treeNodeSet = new LinkedHashSet<>();
        QMediaVO mediaVO = QMediaVO.mediaVO;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        //发行年份
        List<Short>  list = queryFactory
                        .selectFrom(mediaVO)
                        .select(mediaVO.year)
                        .distinct()
                        .orderBy(mediaVO.year.desc())
                        .fetch();
        for(Short year : list){
            TreeNode treeNode = new TreeNode();
            treeNode.setText(String.valueOf(year));
            treeNode.setDataUrl("/mediavo/years/"+String.valueOf(year));
            treeNode.setHtmlUrl("/pages/table_mediaVO?dataUrl="+treeNode.getDataUrl());
            treeNodeSet.add(treeNode);
//            System.out.println(year);
        }

        TreeNode pTreeNode = new TreeNode();
        pTreeNode.setText("发行年份");
        pTreeNode.setDataUrl("/mediavo/years");
        pTreeNode.setHtmlUrl("/pages/table_mediaVO?dataUrl="+pTreeNode.getDataUrl());
        pTreeNode.setChildren(treeNodeSet);
        pTreeNode.setState("closed");
        treeNodeList.add(pTreeNode);



        //收集时间
        List<Date> gatherDateList = queryFactory
                .selectFrom(mediaVO)
                .select(mediaVO.media.gatherDate)
                .fetch();
        List<Date> gatherDateListNew = new ArrayList<>(new HashSet<Date>(gatherDateList));
//        Collections.sort(gatherDateListNew);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        for (Date date : gatherDateListNew){
//            System.out.println(sdf.format(date));
            gatherDateSet.add(sdf.format(date));
        }

        treeNodeSet = new LinkedHashSet<>();
        Iterator<String> iterator = gatherDateSet.iterator();
        while (iterator.hasNext()){
            String yearMonth = iterator.next();
            TreeNode treeNode = new TreeNode();
            treeNode.setText(yearMonth);
            treeNode.setDataUrl("/mediavo/gatherdate/"+yearMonth);
            treeNode.setHtmlUrl("/pages/table_mediaVO?dataUrl="+treeNode.getDataUrl());
            treeNodeSet.add(treeNode);
        }

        pTreeNode = new TreeNode();
        pTreeNode.setText("收集日期");
        pTreeNode.setDataUrl("/mediavo/gatherdates");
        pTreeNode.setHtmlUrl("/pages/table_mediaVO?dataUrl="+pTreeNode.getDataUrl());
        pTreeNode.setChildren(treeNodeSet);
        pTreeNode.setState("closed");
        treeNodeList.add(pTreeNode);


        //数据整理
        treeNodeSet = new LinkedHashSet<>();
        TreeNode treeNode = new TreeNode();
        treeNode.setText("去重");
        treeNode.setDataUrl("/mediavo/repetitive/");
        treeNode.setHtmlUrl("/pages/table_mediaVO?dataUrl="+treeNode.getDataUrl());
        treeNodeSet.add(treeNode);

        treeNode = new TreeNode();
        treeNode.setText("废弃");
        treeNode.setDataUrl("/mediavo/deleted/1");
        treeNode.setHtmlUrl("/pages/table_mediaVO?dataUrl="+treeNode.getDataUrl());
        treeNodeSet.add(treeNode);



        pTreeNode = new TreeNode();
        pTreeNode.setText("数据整理");
        pTreeNode.setDataUrl("");
        pTreeNode.setHtmlUrl("");
        pTreeNode.setChildren(treeNodeSet);
        pTreeNode.setState("closed");
        treeNodeList.add(pTreeNode);


        //数据整理
        treeNodeSet = new LinkedHashSet<>();
        treeNode = new TreeNode();
        treeNode.setText("已关联");
        treeNode.setDataUrl("/filmvo/years/");
        treeNode.setHtmlUrl("/pages/table_filmVO?dataUrl="+treeNode.getDataUrl());
        treeNodeSet.add(treeNode);

        treeNode = new TreeNode();
        treeNode.setText("未关联");
        treeNode.setDataUrl("/mediavo/unrelation/");
        treeNode.setHtmlUrl("/pages/table_mediaVO?dataUrl="+treeNode.getDataUrl());
        treeNodeSet.add(treeNode);

        treeNode = new TreeNode();
        treeNode.setText("导演列表");
        treeNode.setDataUrl("/person/directors");
        treeNode.setHtmlUrl("/pages/table_personVO?dataUrl="+treeNode.getDataUrl());
        treeNodeSet.add(treeNode);

        treeNode = new TreeNode();
        treeNode.setText("演员列表");
        treeNode.setDataUrl("/person/actors");
        treeNode.setHtmlUrl("/pages/table_personVO?dataUrl="+treeNode.getDataUrl());
        treeNodeSet.add(treeNode);

        pTreeNode = new TreeNode();
        pTreeNode.setText("影片详情");
        pTreeNode.setDataUrl("");
        pTreeNode.setHtmlUrl("");
        pTreeNode.setChildren(treeNodeSet);
        pTreeNode.setState("closed");
        treeNodeList.add(pTreeNode);




        return treeNodeList;
    }



}
