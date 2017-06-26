package ff.project.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by F on 2017/6/15.
 * 角色
 */
@Entity
@Table
@Data
public class Role extends BaseEntity{

    private static final long serialVersionUID = 8549799122579486310L;


    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "code", length = 50)
    private String code;

    @Column(name = "remark", length = 1000)
    private String remark;

    @Column(name = "sort")
    private Integer sort;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_function",joinColumns = @JoinColumn(name = "role_id"),inverseJoinColumns = @JoinColumn(name = "function_id"))
    private Set<Function> functions;

    @ManyToMany(fetch = FetchType.EAGER ,mappedBy = "roles")
    private Set<User> users;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        if (!super.equals(o)) return false;

        Role that = (Role) o;

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
        return "Role{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", remark='" + remark + '\'' +
                ", id=" + id +
                ", sort=" + sort +
                ", version=" + version +
                ", createDateTime=" + createDateTime +
                ", updateDateTime=" + updateDateTime +
                '}';
    }
}
