package org.ff.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.ff.entity.User;
import org.ff.pojo.ResultCode;
import org.ff.service.RoleService;
import org.ff.service.UserService;
import org.ff.utils.EncryptUtil;
import org.ff.utils.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

;

@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private RoleService roleService;

    @Resource
    private UserService userService;

    @Value("${page.main}")
    private static String MAIN_PAGE ;

    @Value("${page.login}")
    private static String LOGIN_PAGE;

    @Value("${page.register}")
    private static String REGISTER_PAGE;

    @RequestMapping(value = "/login")
    private String doLogin(HttpServletRequest request, Model model) {
        //已经登录过，直接进入主页
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            boolean isAuthorized = Boolean.valueOf(subject.getSession().getAttribute("isAuthorized").toString());
            if (isAuthorized)
                return MAIN_PAGE;
        }
        String userName = request.getParameter("userName");
        //默认首页，第一次进来
        if (StrUtil.isEmpty(userName)) {
            return LOGIN_PAGE;
        }
        String password = request.getParameter("password");
        //密码加密+加盐
        password = EncryptUtil.getPassword(password, userName);
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        token.setRememberMe(true);
        subject = SecurityUtils.getSubject();
        String msg;
        try {
            subject.login(token);
            //通过认证
            if (subject.isAuthenticated()) {
                Set<String> roles = roleService.getRoleCodeSet(userName);
                if (!roles.isEmpty()) {
                    subject.getSession().setAttribute("isAuthorized", true);
                    return MAIN_PAGE;
                } else {//没有授权
                    msg = "您没有得到相应的授权！";
                    model.addAttribute("message", new ResultCode("1", msg));
                    subject.getSession().setAttribute("isAuthorized", false);
                    LOGGER.error(msg);
                    return LOGIN_PAGE;
                }

            } else {
                return LOGIN_PAGE;
            }
            //0 未授权 1 账号问题 2 密码错误  3 账号密码错误
        } catch (IncorrectCredentialsException e) {
            msg = "登录密码错误. Password for account " + token.getPrincipal() + " was incorrect";
            model.addAttribute("message", new ResultCode("2", msg));
            LOGGER.error(msg);
        } catch (ExcessiveAttemptsException e) {
            msg = "登录失败次数过多";
            model.addAttribute("message", new ResultCode("3", msg));
            LOGGER.error(msg);
        } catch (LockedAccountException e) {
            msg = "帐号已被锁定. The account for username " + token.getPrincipal() + " was locked.";
            model.addAttribute("message", new ResultCode("1", msg));
            LOGGER.error(msg);
        } catch (DisabledAccountException e) {
            msg = "帐号已被禁用. The account for username " + token.getPrincipal() + " was disabled.";
            model.addAttribute("message", new ResultCode("1", msg));
            LOGGER.error(msg);
        } catch (ExpiredCredentialsException e) {
            msg = "帐号已过期. the account for username " + token.getPrincipal() + "  was expired.";
            model.addAttribute("message", new ResultCode("1", msg));
            LOGGER.error(msg);
        } catch (UnknownAccountException e) {
            msg = "帐号不存在. There is no user with username of " + token.getPrincipal();
            model.addAttribute("message", new ResultCode("1", msg));
            LOGGER.error(msg);
        } catch (UnauthorizedException e) {
            msg = "您没有得到相应的授权！" + e.getMessage();
            model.addAttribute("message", new ResultCode("1", msg));
            LOGGER.error(msg);
        }
        return LOGIN_PAGE;
    }


    @RequestMapping(value = "/logout")
    private String doLogout(Model model) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return LOGIN_PAGE;
    }

    @RequestMapping(value="/register",method = RequestMethod.GET)
    public String register(Model model){
        return REGISTER_PAGE;
    }



    /**
     * 校验当前登录名/邮箱的唯一性
     * @param loginName 登录名
     * @param userId 用户ID（用户已经存在，改回原来的名字还是唯一的）
     * @return
     */
    @RequestMapping(value = "/oauth/checkUnique", method = RequestMethod.POST)
    @ResponseBody
    public Map checkExist(String loginName, String userId) {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        User user = userService.findByLoginName(loginName);
        //用户不存在，校验有效
        if (user == null) {
            map.put("valid", true);
        } else {
            if(!StrUtil.isEmpty(userId)&&userId.equals(user.getLoginName())){
                map.put("valid",true);
            }else {
                map.put("valid", false);
            }
        }
        return map;
    }


}
