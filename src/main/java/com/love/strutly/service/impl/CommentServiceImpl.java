package com.love.strutly.service.impl;

import com.love.strutly.aop.annotation.CommentAnnotation;
import com.love.strutly.entity.Comment;
import com.love.strutly.exception.code.BaseExceptionType;
import com.love.strutly.filter.SensitiveFilter;
import com.love.strutly.repository.CommentRepository;
import com.love.strutly.service.CommentService;
import com.love.strutly.utils.BeanMapper;
import com.love.strutly.utils.DataResult;
import com.love.strutly.vo.req.CommentAddReqVO;
import com.love.strutly.vo.req.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lj
 * @Date 2020/11/11 16:03
 * @Version 1.0
 */
@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    @CommentAnnotation
    public Integer save(CommentAddReqVO vo) {
        Comment comment = null;
        if(vo.getType().equals(1)){
            comment = commentRepository.findByRidAndTypeAndUid(vo.getRid(),1,vo.getUid());
            if(comment!=null){
                commentRepository.delete(comment);
                return -1;
            }
        }
        comment = new Comment();
        if(StringUtils.isNotBlank(vo.getContent())){
            vo.setContent(sensitiveFilter.filter(vo.getContent()));
        }
        BeanMapper.mapExcludeNull(vo,comment);
        return commentRepository.save(comment).getId();
    }

    @Override
    public Boolean like(Integer rid, Integer uid) {
        return commentRepository.findByRidAndTypeAndUid(rid,1,uid)==null?false:true;
    }

    @Override
    public List<Comment> findByUidAndType(Integer uid,Integer type) {
        return commentRepository.findByUidAndTypeOrderByIdDesc(uid,type);
    }

    @Override
    public List<Comment> page(PageVO vo) {
        List<Comment> comments = findByUidAndType(vo.getUid(),2);
        return comments.stream().skip((vo.getPageNo())* vo.getPageSize()).limit(vo.getPageSize()).collect(Collectors.toList());
    }

    @Override
    public DataResult delete(Integer id, Integer uid) {
        Comment comment = commentRepository.getOne(id);
        if(comment!=null && comment.getUid().equals(uid)){
            commentRepository.delete(comment);
            return DataResult.success();
        }
        return DataResult.getResult(BaseExceptionType.USER_ERROR.getCode(),"您没有权限进行此操作!");
    }
}
