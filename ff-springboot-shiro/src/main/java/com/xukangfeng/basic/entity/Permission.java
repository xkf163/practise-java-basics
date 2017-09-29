package com.xukangfeng.basic.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by xukangfeng on 2017/8/21 21:47
 * 权限信息
 */
@Entity
@Table(name = "SysPermission")
@Data
public class Permission implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;//主键.

    private String name;//名称.

    @Column(columnDefinition="enum('menu','button')")
    private String resourceType;//资源类型，[menu|button]

    private String url;//资源路径.

    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view

    private Long parentId; //父编号

    private String parentIds; //父编号列表

    private Boolean available = Boolean.FALSE;

    @ManyToMany
    @JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="permissionId")},inverseJoinColumns={@JoinColumn(name="roleId")})
    private List<Role> roles;
}
