package ff.project.domain;

import lombok.Data;

import javax.persistence.MappedSuperclass;

/**
 * Created by F on 2017/6/15.
 * 人类
 */
@MappedSuperclass
@Data
public class Human extends BaseModel {

    private String name;

    private Integer age;

    private String sex;

    private String country;
}
