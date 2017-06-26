package ff.project.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by F on 2017/6/15.
 * 演员
 */
@Entity
@Table
@Data
public class Actor extends Human {


    @ManyToMany
    @JoinTable(name = "film_actor",joinColumns = @JoinColumn(name = "actor_id"),inverseJoinColumns = @JoinColumn(name = "film_id"))
    private Set<Film> films;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Actor)) return false;
        if (!super.equals(o)) return false;

        Actor baseModel = (Actor) o;

        return getId().equals(baseModel.getId());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }

}
