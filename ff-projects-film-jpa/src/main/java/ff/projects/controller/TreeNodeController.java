package ff.projects.controller;

import ff.projects.common.TreeNode;
import ff.projects.service.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by F on 2017/6/23.
 */
@RestController
public class TreeNodeController {


    @Autowired
    TreeService treeService;

    @GetMapping(value = "/initTree")
    public List<TreeNode> initTree(@RequestParam(required = false,name = "id") String nodeId){

        List<TreeNode> treeNodeList = new ArrayList<>();
        //表示打开首页动作，初始化1级节点
        if (nodeId == null) {
            //1级父节点
            TreeNode pTreeNode;
            pTreeNode = new TreeNode("010000","收集年月", "closed", "/pages/table_mediaVO?dataUrl=/mediavo/gatherdates", "/mediavo/gatherdates" );
            treeNodeList.add(pTreeNode);

            pTreeNode = new TreeNode("020000","影片年代", "closed", "/pages/table_mediaVO?dataUrl=/mediavo/years", "/mediavo/years");
            treeNodeList.add(pTreeNode);

            pTreeNode = new TreeNode("030000","影人作品", "closed", "", "" );
            treeNodeList.add(pTreeNode);

            pTreeNode = new TreeNode("040000","操作平台", "closed", "", "" );
            treeNodeList.add(pTreeNode);
        }

        if("010000".equals(nodeId)){
            treeNodeList = new ArrayList<>(treeService.getSonTreeGroupByGatherDate());
        }

        if("020000".equals(nodeId)){
            treeNodeList = new ArrayList<>(treeService.getSonTreeGroupByMovieReleaseYear());
        }
        if("030000".equals(nodeId)){
            treeNodeList = new ArrayList<>(treeService.getSonTreeGroupByStar());
        }
        if("040000".equals(nodeId)){
            treeNodeList = new ArrayList<>(treeService.getTreeForAdmin());
        }

        return treeNodeList;
    }


    //弃用
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
        treeNode.setText("已有关联");
        treeNode.setDataUrl("/filmvo/years/");
        treeNode.setHtmlUrl("/pages/table_filmVO?dataUrl="+treeNode.getDataUrl());
        treeNodeSet.add(treeNode);

        treeNode = new TreeNode();
        treeNode.setText("未有关联");
        treeNode.setDataUrl("/mediavo/unrelation/");
        treeNode.setHtmlUrl("/pages/table_mediaVO?dataUrl="+treeNode.getDataUrl());
        treeNodeSet.add(treeNode);

        treeNode = new TreeNode();
        treeNode.setText("重复下载");
        treeNode.setDataUrl("/mediavo/repetitive/");
        treeNode.setHtmlUrl("/pages/table_mediaVO?toolbarType=toolbar_buttonWithqueryWithoutDate&dataUrl="+treeNode.getDataUrl());
        treeNodeSet.add(treeNode);

        treeNode = new TreeNode();
        treeNode.setText("增删改查");
        treeNode.setDataUrl("/mediavo/gatherdates");
        treeNode.setHtmlUrl("/pages/table_mediaVO?toolbarType=toolbar_add&dataUrl="+treeNode.getDataUrl());
        treeNodeSet.add(treeNode);

        treeNode = new TreeNode();
        treeNode.setText("回收站");
        treeNode.setDataUrl("/mediavo/deleted/1");
        treeNode.setHtmlUrl("/pages/table_mediaVO?toolbarType=toolbar_recycle&dataUrl="+treeNode.getDataUrl());
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
        treeNode.setText("导演列表");
        treeNode.setDataUrl("/person/directors");
        treeNode.setHtmlUrl("/pages/table_personVO?toolbarType=toolbar_person&dataUrl="+treeNode.getDataUrl());
        treeNodeSet.add(treeNode);

        treeNode = new TreeNode();
        treeNode.setText("演员列表");
        treeNode.setDataUrl("/person/actors");
        treeNode.setHtmlUrl("/pages/table_personVO?toolbarType=toolbar_person&dataUrl="+treeNode.getDataUrl());
        treeNodeSet.add(treeNode);

        pTreeNode = new TreeNode();
        pTreeNode.setText("影人作品");
        pTreeNode.setDataUrl("");
        pTreeNode.setHtmlUrl("");
        pTreeNode.setChildren(treeNodeSet);
        pTreeNode.setState("closed");
        treeNodeList.add(pTreeNode);




        return treeNodeList;
    }



}
