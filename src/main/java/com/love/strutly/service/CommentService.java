package com.love.strutly.service;

import com.love.strutly.entity.Comment;
import com.love.strutly.vo.req.CommentAddReqVO;

import java.util.List;

public interface CommentService {
    void save(CommentAddReqVO vo);
    Boolean like(Integer rid,Integer uid);

    List<Comment> findByUidAndType(Integer uid,Integer type);
}
