package com.love.strutly.aop.aspect;

import com.love.strutly.entity.Notice;
import com.love.strutly.entity.Record;
import com.love.strutly.repository.NoticeRepository;
import com.love.strutly.repository.RecordRepository;
import com.love.strutly.utils.BeanMapper;
import com.love.strutly.vo.req.CommentAddReqVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author lj
 * @Date 2020/11/22 15:36
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
public class CommentAspect {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Pointcut("@annotation(com.love.strutly.aop.annotation.CommentAnnotation)")
    public void noticePointCut(){

    }

    @After(value = "noticePointCut()")
    public void insertLog(JoinPoint joinPoint) throws Throwable {
        Object obj = joinPoint.getArgs()[0];
        if(obj==null)
            return;
        CommentAddReqVO vo = BeanMapper.map(obj,CommentAddReqVO.class);
        if(vo.getType().equals(2)){
            Record record = recordRepository.getOne(vo.getRid());
            Notice notice = new Notice();
            BeanMapper.mapExcludeNull(vo,notice);
            notice.setOid(record.getMiniUser().getId());
            noticeRepository.save(notice);
            if(vo.getOid()!=null){
                notice = new Notice();
                BeanMapper.mapExcludeNull(vo,notice);
                notice.setOid(vo.getOid());
                notice.setContent(vo.getNickName()+"回复了你:"+vo.getContent());
                noticeRepository.save(notice);
            }
        }
    }
}
