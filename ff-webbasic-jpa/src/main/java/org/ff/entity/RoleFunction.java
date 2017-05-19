package org.ff.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_role_function")
public class RoleFunction extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = -1340123834197348115L;

    @Column(name = "roleId", length = 36)
    private String roleId;

    @Column(name = "functionId", length = 36)
    private String functionId;

    @Column(name="remark")
    private String remark;


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFunctionId() {

        return functionId;
    }

    public void setFunctionId(String functionId) {

        this.functionId = functionId;
    }

    public String getRoleId() {

        return roleId;
    }

    public void setRoleId(String roleId) {

        this.roleId = roleId;
    }

}
