package com.love.strutly.vo.req;

import lombok.Data;

/**
 * @Author lj
 * @Date 2020/11/11 14:49
 * @Version 1.0
 */
@Data
public class CommentAddReqVO {
    private Integer uid;//用户id
    private String nickName;//评论者昵称
    private String avatarUrl;//评论用户的头像

    private Integer rid;//记录id
    private String msg;//标题
    private String pic;//第一张图

    private Integer oid;//回复对象id
    private String otherName;//回复对象名称
    private String content;//回复内容


    private Integer type;//1评论 2点赞,3分享
}
