package ff.projects.controller;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ff.projects.common.TreeNode;
import ff.projects.entity.QMediaVO;
import ff.projects.repository.MediaVORepository;
import ff.projects.service.TreeService;
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


    @Autowired
    TreeService treeService;

    @GetMapping(value = "/tree")
    public List<TreeNode> listTree(){
        List<TreeNode> treeNodeList = new ArrayList<>();
        treeNodeList.add(treeService.getTreeGroupByGatherDate());
        treeNodeList.add(treeService.getTreeGroupByMovieReleaseYear());


        Set<TreeNode> treeNodeSet = null;
        TreeNode pTreeNode = null;
        TreeNode treeNode = null;

        //树3：操作平台------------s------------------
        treeNodeSet = new LinkedHashSet<>();
        treeNode = new TreeNode();
        treeNode.setText("重复下载");
        treeNode.setDataUrl("/mediavo/repetitive/");
        treeNode.setHtmlUrl("/pages/table_mediaVO?dataUrl="+treeNode.getDataUrl());
        treeNodeSet.add(treeNode);

        treeNode = new TreeNode();
        treeNode.setText("增删改查");
        treeNode.setDataUrl("/mediavo/deleted/1");
        treeNode.setHtmlUrl("/pages/table_mediaVO?dataUrl="+treeNode.getDataUrl());
        treeNodeSet.add(treeNode);

        treeNode = new TreeNode();
        treeNode.setText("回收站");
        treeNode.setDataUrl("/mediavo/deleted/1");
        treeNode.setHtmlUrl("/pages/table_mediaVO?dataUrl="+treeNode.getDataUrl());
        treeNodeSet.add(treeNode);

        pTreeNode = new TreeNode();
        pTreeNode.setText("操作平台");
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
        pTreeNode.setText("电影资料详情");
        pTreeNode.setDataUrl("");
        pTreeNode.setHtmlUrl("");
        pTreeNode.setChildren(treeNodeSet);
        pTreeNode.setState("closed");
        treeNodeList.add(pTreeNode);




        return treeNodeList;
    }



}
