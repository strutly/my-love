package com.love.strutly.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

/**
 * @Author lj
 * 说说记录表
 * @Date 2020/11/8 17:51
 * @Version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "love_record")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Record extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="uid",columnDefinition ="int(11) comment '用户id'")//指定外键名称
    private MiniUser miniUser;

    private String msg;//内容

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<String> imgs;

    private Boolean open;
}
