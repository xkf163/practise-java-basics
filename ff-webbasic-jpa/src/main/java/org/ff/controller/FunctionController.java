package org.ff.controller;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.ff.entity.Function;
import org.ff.entity.User;
import org.ff.pojo.TreeNode;
import org.ff.service.FunctionService;
import org.ff.service.RoleService;
import org.ff.utils.StrUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/function")
public class FunctionController {

    @Resource
    private FunctionService functionService;


    @Resource
    private RoleService roleService;
    /**
     * 用户列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/tree")
    private String list() {
        return "base/auth/function_tree";
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    @ResponseBody
    public List<Function> getAll() {
        return functionService.findAllByOrderByLevelCode();
    }


    @RequestMapping(value = "/user" , method = RequestMethod.POST)
    @ResponseBody
    public List<Function> findByUserName(){
       Subject subject = SecurityUtils.getSubject();
       User user =(User) subject.getPrincipal();
        Set<String> roleCodes = roleService.findRoleCodeSetByUserName(user.getName());
        return functionService.findFSetByRoleCode(roleCodes);
    }


//    /**
//     * getTreeData 构造bootstrap-treeview格式数据
//     *
//     * @return
//     */
//    @RequestMapping(value = "/treeData", method = RequestMethod.POST)
//    @ResponseBody
//    public List<TreeNode> getTreeData() {
//
//        return functionService.getTreeData();
//    }
//
//    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
//    @ResponseBody
//    public Function get(@PathVariable("id") String id) {
//
//        Function function = functionService.get(Function.class, id);
//        if (!StrUtil.isEmpty(function.getParentId())) {
//            function.setParentName(functionService.get(Function.class, function.getParentId()).getName());
//        } else {
//            function.setParentName("系统菜单");
//        }
//        return function;
//    }
//
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    @ResponseBody
//    public Result save(Function function) {
//
//        function.setUpdateDateTime(new Date());
//        functionService.saveOrUpdate(function);
//        return new Result(true);
//    }
//
//    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
//    @ResponseBody
//    public Result delete(@PathVariable("id") String id) {
//
//        try {
//            Function function = functionService.get(Function.class, id);
//            functionService.delete(function);
//            return new Result(true);
//        } catch (Exception e) {
//            return new Result(false, "该菜单/功能已经被其他数据引用，不可删除");
//        }
//    }
//
//
//    //TODO 功能集合将从session中获取
//    @RequestMapping(value="/navigation")
//    @ResponseBody
//    public List<Function> navigation(String pageUrl){
//        return  functionService.getAll();
//    }


}
