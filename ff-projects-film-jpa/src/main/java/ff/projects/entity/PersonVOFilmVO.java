package ff.projects.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by F on 2017/6/28.
 */

@Entity
@Table(name = "relation_person_film", uniqueConstraints = {@UniqueConstraint(name = "pf_cons",columnNames = {"id"})},indexes = {@Index(name = "pf_index",columnList = "id")})
@Data
public class PersonVOFilmVO implements Serializable{

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Person person;

    private String asDirector;

    private String asActor;

    private Integer asDirectorNumber = 0;

    private Integer asActorNumber = 0;


}
