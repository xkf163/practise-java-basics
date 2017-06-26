package ff.projects.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by F on 2017/6/22.
 */
@Entity
@Table(indexes = {@Index(name = "media_vo_index",columnList = "nameChn,nameEng,year")} )
@Data
public class MediaVO implements Serializable{

    @Id
    @GeneratedValue
    private Long id;

    private String nameChn;

    private String nameEng;

    private Short year;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_id")
    private Media media;

}
