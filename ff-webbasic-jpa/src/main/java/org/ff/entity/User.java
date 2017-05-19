package org.ff.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = "tbl_user")
public class User extends BaseEntity {

    private static final long serialVersionUID = 6093546087036436583L;


    @Column(name = "name")
    private String name;

    @Column(name = "sex")
    private String sex;


    @Column(name = "birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;


    @Column(name = "login_name")
    private String loginName;

    @Column(name = "password")
    private String password;


    @Column(name = "email", length = 50)
    private String email;


    @Column(name = "telphone")
    private String telphone;


    @Column(name = "mobile")
    private String mobile;


    @Column(name = "qq")
    private String qq;


    @Column(name = "wechat")
    private String wechat;


    @Column(name = "open_account", length = 5)
    private String openAccount;


    @Column(name = "isSuperAdmin")
    private String isSuperAdmin;


    @Transient
    private String avatarId;

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getSex() {

        return sex;
    }

    public void setSex(String sex) {

        this.sex = sex;
    }

    public Date getBirthday() {

        return birthday;
    }

    public void setBirthday(Date birthday) {

        this.birthday = birthday;
    }

    public String getLoginName() {

        return loginName;
    }

    public void setLoginName(String loginName) {

        this.loginName = loginName;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getTelphone() {

        return telphone;
    }

    public void setTelphone(String telphone) {

        this.telphone = telphone;
    }

    public String getMobile() {

        return mobile;
    }

    public void setMobile(String mobile) {

        this.mobile = mobile;
    }

    public String getQq() {

        return qq;
    }

    public void setQq(String qq) {

        this.qq = qq;
    }

    public String getWechat() {

        return wechat;
    }

    public void setWechat(String wechat) {

        this.wechat = wechat;
    }

    public String getOpenAccount() {

        return openAccount;
    }

    public void setOpenAccount(String openAccount) {

        this.openAccount = openAccount;
    }

    public String getIsSuperAdmin() {

        return isSuperAdmin;
    }

    public void setIsSuperAdmin(String isSuperAdmin) {

        this.isSuperAdmin = isSuperAdmin;
    }

}
