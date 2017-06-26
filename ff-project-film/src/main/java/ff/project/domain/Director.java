package ff.project.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by F on 2017/6/15.
 * 导演
 */
@Entity
@Table
@Data
public class Director extends Human {

    @ManyToMany
    @JoinTable(name = "film_director",joinColumns = @JoinColumn(name = "director_id"),inverseJoinColumns = @JoinColumn(name = "film_id"))
    private Set<Film> films;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Director)) return false;
        if (!super.equals(o)) return false;

        Director baseModel = (Director) o;

        return getId().equals(baseModel.getId());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }

}
