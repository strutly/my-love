package com.love.strutly.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author lj
 * 粉丝表
 * @Date 2020/11/8 17:59
 * @Version 1.0
 */
@Data
@Entity
@Table(name = "love_fans")
public class Fans extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="uid",columnDefinition ="int(11) comment '用户id'")//指定外键名称
    private MiniUser mine;//关注者
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="oid",columnDefinition ="int(11) comment '用户id'")//指定外键名称
    private MiniUser to;//被关注者
}
