package ff.projects.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by F on 2017/6/27.
 */
@Entity
@Table(name = "fm_person",uniqueConstraints = {@UniqueConstraint(name = "person_cons",columnNames = {"doubanNo","name"})},indexes = {@Index(name = "person_index",columnList = "name,nameExtend,doubanNo")})
@Data
public class Person implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String nameExtend;

    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    private String info;

    @Column(columnDefinition = "TEXT")
    private String introduce;

    private String doubanNo;


    private String gender;
    private String birthDay;
    private String birthPlace;
    private String job;
    private String imdbNo;


}
