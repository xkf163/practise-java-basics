package org.ff.entity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_user_role")
public class UserRole extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 2836972845793033228L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "roleId", length = 36)
    private String roleId;

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }

    public String getRoleId() {

        return roleId;
    }

    public void setRoleId(String roleId) {

        this.roleId = roleId;
    }

}
