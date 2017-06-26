package ff.project.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by F on 2017/6/15.
 * 电影实体类
 */
@Entity
@Table
@Data
public class Film extends BaseModel {


    private String name;

    private Integer year;

    @ManyToMany
    @JoinTable(name = "film_director",joinColumns = @JoinColumn(name = "film_id"),inverseJoinColumns = @JoinColumn(name = "director_id"))
    private Set<Director> directors;

    @ManyToMany
    @JoinTable(name = "film_actor",joinColumns = @JoinColumn(name = "film_id"),inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private Set<Actor> actors;


    @Column(columnDefinition = "TEXT")
    private String content;

    //创建日期
    private Date createAt;

    //更新（修改）日期
    private Date modifiedAt;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        if (!super.equals(o)) return false;

        Film baseModel = (Film) o;

        return getId().equals(baseModel.getId());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }

}
