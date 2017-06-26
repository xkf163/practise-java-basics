package org.ff.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_role")
public class Role extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 8549799122579486310L;


    @Column(name = "name", length = 50)
    private String name;


    @Column(name = "code", length = 50)
    private String code;


    @Column(name = "remark", length = 1000)
    private String remark;


    @Column(name = "sort")
    private Integer sort;





    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public String getRemark() {

        return remark;
    }

    public void setRemark(String remark) {

        this.remark = remark;
    }

    public Integer getSort() {

        return sort;
    }

    public void setSort(Integer sort) {

        this.sort = sort;
    }

}
