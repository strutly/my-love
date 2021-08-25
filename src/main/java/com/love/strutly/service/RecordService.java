package com.love.strutly.service;

import com.love.strutly.entity.Record;
import com.love.strutly.utils.DataResult;
import com.love.strutly.vo.req.PageVO;
import com.love.strutly.vo.req.RecordAddReqVO;
import com.love.strutly.vo.resp.record.RecordDetailRespVO;
import org.springframework.data.domain.Page;

/**
 * @Author lj
 * @Date 2020/11/9 11:53
 * @Version 1.0
 */
public interface RecordService {
    void save(RecordAddReqVO vo);
    RecordDetailRespVO getOne(Integer id);
    Page<Record> list(PageVO vo);
    //void saveAll();
    DataResult open(Integer id,Integer uid);

    DataResult delete(Integer id, Integer uid);
}
