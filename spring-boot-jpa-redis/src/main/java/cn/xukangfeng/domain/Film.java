package cn.xukangfeng.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by F on 2017/5/14.
 */
@Entity
@Table
public class Film extends BaseEntity {

    //片名
    private String name;

    //导演
    private Set<Object> directors;

    //演员
    private Set<Object> actors;

    //发行年代
    private short year;

    //评分
    private float score;


}
