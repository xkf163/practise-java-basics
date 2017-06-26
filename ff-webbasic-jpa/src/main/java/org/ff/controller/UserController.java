package org.ff.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.ff.entity.User;
import org.ff.service.UserService;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    private String list() {

        return "base/user/user_list";
    }

    /**
     * 用户编辑
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/edit")
    private String edit(String id, HttpServletRequest request) {

        request.setAttribute("id", id);
        return "base/user/user_edit";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    private User getUser(String id) {

        return userService.findById(id);
    }


    /**
     * tab方式curd demo
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/tab/list")
    private String tablist() {

        return "base/user/user_tab_list";
    }


    /**
     * 新的页面打开 curd demo
     */
    @RequestMapping(method = RequestMethod.GET, value = "/page/list")
    private String pagelist(String id, HttpServletRequest request) {
        request.setAttribute("selectId", id);
        return "base/user/user_page_list";
    }


    /**
     * 用户头像上传 avatar
     */
    @RequestMapping(method = RequestMethod.GET, value = "/avatar")
    private String avatar(String userId, HttpServletRequest request) {
        request.setAttribute("userId", userId);
        return "base/user/user_avatar";
    }




}
