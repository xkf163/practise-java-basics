package org.ff.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by F on 2017/6/15.
 */

@MappedSuperclass
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id",strategy = "identity")
    @GeneratedValue(generator = "id")
    protected Long id;

    /**
     * 版本号
     */
    @Version
    protected Integer version;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    protected Date createDateTime;

    /**
     * 最后修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    protected Date updateDateTime;

    /**
     * 删除标记(0启用，1禁用)
     */
    private Integer deleted;



}
