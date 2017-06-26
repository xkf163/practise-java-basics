package ff.project.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by F on 2017/6/15.
 */
@Entity
@Table
@Data
public class User extends BaseEntity {

    private static final long serialVersionUID = 6093546087036436583L;

    private String name;

    private String sex;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private String loginName;

    private String password;

    @Column(length = 50)
    private String email;

    private String telphone;

    private String mobile;

    private String qq;

    private String wechat;

    @Column(length = 5)
    private String openAccount;

    private String isSuperAdmin;

    @ManyToMany
    @JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;

        User that = (User) o;

        if (!getId().equals(that.getId())) return false;
        return getVersion() != null ? getVersion().equals(that.getVersion()) : that.getVersion() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getId().hashCode();
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", loginName='" + loginName + '\'' +
                ", id=" + id +
                ", password='" + password + '\'' +
                ", version=" + version +
                ", email='" + email + '\'' +
                ", telphone='" + telphone + '\'' +
                ", mobile='" + mobile + '\'' +
                ", qq='" + qq + '\'' +
                ", wechat='" + wechat + '\'' +
                ", openAccount='" + openAccount + '\'' +
                ", createDateTime=" + createDateTime +
                ", isSuperAdmin='" + isSuperAdmin + '\'' +
                ", updateDateTime=" + updateDateTime +
                '}';
    }
}
