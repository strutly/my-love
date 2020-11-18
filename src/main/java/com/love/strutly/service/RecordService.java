package com.love.strutly.service;

import com.love.strutly.vo.req.PageVO;
import com.love.strutly.vo.req.RecordAddReqVO;
import com.love.strutly.vo.resp.record.RecordDetailRespVO;
import com.love.strutly.vo.resp.record.RecordListRespVO;

import java.util.List;

/**
 * @Author lj
 * @Date 2020/11/9 11:53
 * @Version 1.0
 */
public interface RecordService {
    void save(RecordAddReqVO vo);

    RecordDetailRespVO getOne(Integer id);

    List<RecordListRespVO> page(PageVO vo);


    List<RecordListRespVO> myList(PageVO vo);

    void open(Integer id);

    void delete(Integer id);
}
