package ff.projects.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by F on 2017/11/1.
 * 明星：medias包含的所有person
 */
@Entity
@Data
public class Star implements Serializable{

    @Id
    @GeneratedValue
    private Long id;

    private String douBanNo;

    private Long personId;

    private String asDirector;

    private String asActor;

    private Integer asDirectorNumber = 0;

    private Integer asActorNumber = 0;

}
