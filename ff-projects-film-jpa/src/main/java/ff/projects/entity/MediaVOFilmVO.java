package ff.projects.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by F on 2017/6/28.
 */

@Entity
@Table(name = "relation_media_film", uniqueConstraints = {@UniqueConstraint(name = "mf_cons",columnNames = {"mediaVOId","filmVOId"})},indexes = {@Index(name = "mf_index",columnList = "mediaVOId,filmVOId")})
@Data
public class MediaVOFilmVO implements Serializable{

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private Long mediaVOId;


    private Long filmVOId;

}
