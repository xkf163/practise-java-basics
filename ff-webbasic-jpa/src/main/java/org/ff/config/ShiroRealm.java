package org.ff.config;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.ff.entity.User;
import org.ff.service.FunctionService;
import org.ff.service.RoleService;
import org.ff.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by F on 2017/5/18.
 */
public class ShiroRealm extends AuthorizingRealm {


    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    FunctionService functionService;


    @Value("${system.version}")
    private String systemVersion;

    /**
     * 登录认证
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用 shiro 权限控制有三种
     * 1、通过xml配置资源的权限
     * 2、通过shiro标签控制权限
     * 3、通过shiro注解控制权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("parameters principals is null");
        }
        //获取已认证的用户名（登录名）
        String username=(String)super.getAvailablePrincipal(principals);
        Set<String> roleCodes=roleService.findRoleCodeSetByUserName(username);
        Set<String> functionCodes=functionService.findUrlSetByRoleCode(roleCodes);
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roleCodes);
        authorizationInfo.setStringPermissions(functionCodes);
        return authorizationInfo;
    }

    /**
     * 权限认证（为当前登录的Subject授予角色和权限）
     *
     * 该方法的调用时机为需授权资源被访问时，并且每次访问需授权资源都会执行该方法中的逻辑，这表明本例中并未启用AuthorizationCache，
     * 如果连续访问同一个URL（比如刷新），该方法不会被重复调用，Shiro有一个时间间隔（也就是cache时间，在ehcache-shiro.xml中配置），
     * 超过这个时间间隔再刷新页面，该方法会被执行
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        if (authcToken == null)
            throw new AuthenticationException("parameter token is null");
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        // 校验用户名密码
        String password=String.copyValueOf(token.getPassword());
        User user= userService.findByLoginName(token.getUsername());
        if (user!=null) {
            if(!password.equals(user.getPassword())&& isNeedPassword()){
                throw new IncorrectCredentialsException();
            }
            // 注意此处的返回值没有使用加盐方式,如需要加盐，可以在密码参数上加
            return new SimpleAuthenticationInfo(user, token.getPassword(), token.getUsername());
        }
        throw new UnknownAccountException();
    }


    //是否需要校验密码登录，用于开发环境 0默认为开发环境，其他为正式环境（1，或者不配）
    public boolean isNeedPassword(){
        if("0".equals(systemVersion))
            return false;
        else
            return true;
    }
}
