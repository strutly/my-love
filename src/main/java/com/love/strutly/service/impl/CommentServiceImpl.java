package com.love.strutly.service.impl;

import com.love.strutly.entity.Comment;
import com.love.strutly.repository.CommentRepository;
import com.love.strutly.service.CommentService;
import com.love.strutly.utils.BeanMapper;
import com.love.strutly.vo.req.CommentAddReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lj
 * @Date 2020/11/11 16:03
 * @Version 1.0
 */
@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void save(CommentAddReqVO vo) {
        Comment comment = null;
        if(vo.getType().equals(1)){
            comment = commentRepository.findByRidAndTypeAndUid(vo.getRid(),1,vo.getUid());
            if(comment!=null){
                commentRepository.delete(comment);
                return;
            }
        }
        comment = new Comment();
        BeanMapper.mapExcludeNull(vo,comment);
        commentRepository.save(comment);
    }

    @Override
    public Boolean like(Integer rid, Integer uid) {
        return commentRepository.findByRidAndTypeAndUid(rid,1,uid)==null?false:true;
    }

    @Override
    public List<Comment> findByUidAndType(Integer uid,Integer type) {
        return commentRepository.findByUidAndTypeOrderByIdDesc(uid,type);
    }
}
