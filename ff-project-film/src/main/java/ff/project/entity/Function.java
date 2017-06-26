package ff.project.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by F on 2017/6/15.
 * 权限（资源）
 */
@Entity
@Table
@Data
public class Function extends BaseEntity {

    private static final long serialVersionUID = 7823895619744279485L;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String code;

    @Column(length = 200)
    private String url;

    private String parentId;

    @Column(length = 36)
    private String levelCode;

    private String icon;

    //菜单类型 0=目录 1=功能 2=按钮
    private String functype;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @Transient
    private String parentName;


    @ManyToMany(mappedBy = "functions")
    private Set<Role> roles;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Function)) return false;
        if (!super.equals(o)) return false;

        Function that = (Function) o;

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
        return "Function{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", url='" + url + '\'' +
                ", parentId='" + parentId + '\'' +
                ", levelCode='" + levelCode + '\'' +
                ", icon='" + icon + '\'' +
                ", functype='" + functype + '\'' +
                ", id=" + id +
                ", remark='" + remark + '\'' +
                ", version=" + version +
                ", parentName='" + parentName + '\'' +
                ", createDateTime=" + createDateTime +
                ", updateDateTime=" + updateDateTime +
                '}';
    }
}
