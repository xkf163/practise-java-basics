package ff.project.framework.filter;

import ff.project.entity.User;
import ff.project.service.FunctionService;
import ff.project.service.RoleService;
import ff.project.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

/**
 * Created by F on 2017/6/16.
 */

public class SystemAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private FunctionService functionService;

    @Value("${system.needPassword}")
    private String needPassword;


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
        String loginName=(String)super.getAvailablePrincipal(principals);
        Set<String> roleCodes=roleService.findCodeSetByLoginName(loginName);
        Set<String> functionCodes=functionService.findCodeSetByRoleCodeSet(roleCodes);
//        Set<String> roleCodes = new HashSet<>();
//        Set<String> functionCodes = new HashSet<>();
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roleCodes);
        authorizationInfo.setStringPermissions(functionCodes);
        return authorizationInfo;
    }

    /**
     * 用户认证
     * @param authcToken 含登录名密码的信息
     * @return 认证信息
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
        if("0".equals(needPassword))
            return false;
        else
            return true;
    }
}
