package com.love.strutly.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author lj
 * 评论,点赞,分享表
 * @Date 2020/11/8 17:55
 * @Version 1.0
 */
@Data
@Entity
@Table(name = "love_comment")
public class Comment extends BaseEntity{
    private Integer rid;//记录id
    private String msg;//标题
    private String pic;//标题
    private String nickName;//评论者昵称
    private String avatarUrl;//评论用户的头像


    private Integer uid;//用户id
    private Integer oid;//回复对象id
    private String otherName;//回复对象名称
    private String content;//回复内容


    private Integer type;//1评论 2点赞,3分享
}
