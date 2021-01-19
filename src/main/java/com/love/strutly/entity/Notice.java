package com.love.strutly.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author lj
 * 通知信息
 * @Date 2020/11/8 18:47
 * @Version 1.0
 */
@Data
@Entity
@Table(name = "love_notice")
public class Notice extends BaseEntity{
    private Integer uid;//评论者
    private String nickName;//评论者的昵称
    private String avatarUrl;//评论者头像
    private Integer oid;//评论谁
    private Integer rid;//内容id
    private String msg;//内容msg
    private String content;//回复的内容
    private String pic;//内容封面图
}
