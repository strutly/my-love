package com.love.strutly.service;

import com.love.strutly.entity.Comment;
import com.love.strutly.utils.DataResult;
import com.love.strutly.vo.req.CommentAddReqVO;
import com.love.strutly.vo.req.PageVO;

import java.util.List;

public interface CommentService {
    Integer save(CommentAddReqVO vo);
    Boolean like(Integer rid,Integer uid);

    List<Comment> findByUidAndType(Integer uid,Integer type);

    List<Comment> page(PageVO vo);

    DataResult delete(Integer id, Integer uid);
}
