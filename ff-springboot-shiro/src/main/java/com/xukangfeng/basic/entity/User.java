package com.xukangfeng.basic.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by 57257 on 2017/8/21.
 * 用户信息
 */
@Entity
@Table(name = "SysUser")
@Data
public class User implements Serializable{


    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique =true)
    private String username;//帐号

    private String name;//名称（昵称或者真实姓名，不同系统不同定义）

    private String password; //密码;

    private String salt;//加密密码的盐

    private byte state;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.

    @ManyToMany(fetch= FetchType.EAGER)//立即从数据库中进行加载数据;
    @JoinTable(name = "SysUserRole", joinColumns = { @JoinColumn(name = "uid") }, inverseJoinColumns ={@JoinColumn(name = "roleId") })
    private List<Role> roles;// 一个用户具有多个角色

    public String getCredentialsSalt(){
        return this.username+this.salt;
    }

}
