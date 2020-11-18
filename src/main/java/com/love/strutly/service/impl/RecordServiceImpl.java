package com.love.strutly.service.impl;

import com.google.common.collect.Lists;
import com.love.strutly.entity.Comment;
import com.love.strutly.entity.Record;
import com.love.strutly.repository.CommentRepository;
import com.love.strutly.repository.RecordRepository;
import com.love.strutly.service.RecordService;
import com.love.strutly.utils.BeanMapper;
import com.love.strutly.utils.RedisUtil;
import com.love.strutly.vo.req.PageVO;
import com.love.strutly.vo.req.RecordAddReqVO;
import com.love.strutly.vo.resp.record.RecordDetailRespVO;
import com.love.strutly.vo.resp.record.RecordListRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lj
 * @Date 2020/11/9 16:09
 * @Version 1.0
 */
@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private CommentRepository commentRepository;


    @Resource
    private RedisUtil redisUtil;

    @Override
    public void save(RecordAddReqVO vo) {
        Record record = new Record();
        BeanMapper.mapExcludeNull(vo,record);
        record = recordRepository.save(record);

        if(record.getOpen()){
            List<Record> records = Lists.newArrayList();
            if(redisUtil.hasKey("open-record")){
                records = (List<Record>) redisUtil.getList("open-record",Record.class);
                records.add(0,record);
                redisUtil.setList("open-record", records,5*60);
            }
        }
    }

    @Override
    public RecordDetailRespVO getOne(Integer id) {
        RecordDetailRespVO recordDetailRespVO = new RecordDetailRespVO();
        Record record = recordRepository.getOne(id);
        if(record!=null){
            recordDetailRespVO = BeanMapper.map(record,RecordDetailRespVO.class);
            List<Comment> records = commentRepository.findByRid(id);
            recordDetailRespVO.setCounts(records.stream().collect(Collectors.groupingBy(s -> s.getType())));
        }
        return recordDetailRespVO;
    }

    @Override
    public List<RecordListRespVO> page(PageVO vo) {
        List<Record> records = Lists.newArrayList();
        if(redisUtil.hasKey("open-record")){
            records = (List<Record>) redisUtil.getList("open-record",Record.class);
        }else{
            records = recordRepository.findByOpenIsTrueOrderByCreateTimeDesc();
            if(records!=null && records.size()>0)
                redisUtil.setList("open-record", records,5*60);
        }
        List<Record> s = records.stream().skip((vo.getPageNo())* vo.getPageSize()).limit(vo.getPageSize()).collect(Collectors.toList());
        return BeanMapper.mapList(s,RecordListRespVO.class);
    }

    @Override
    public List<RecordListRespVO> myList(PageVO vo) {
        List<Record> records = Lists.newArrayList();
        if(vo.getMine()){
            records = recordRepository.findByMiniUser_IdOrderByCreateTimeDesc(vo.getUid());
        }else{
            if(redisUtil.hasKey("records"+vo.getUid())){
                records = (List<Record>) redisUtil.getList("records"+vo.getUid(),Record.class);
            }else{
                records = recordRepository.findByOpenIsTrueAndMiniUser_IdOrderByCreateTimeDesc(vo.getUid());
                if(records!=null && records.size()>0)
                    redisUtil.setList("records"+vo.getUid(), records,5*60);
            }
        }
        List<Record> s = records.stream().skip((vo.getPageNo())* vo.getPageSize()).limit(vo.getPageSize()).collect(Collectors.toList());
        return BeanMapper.mapList(s,RecordListRespVO.class);
    }

    @Override
    public void open(Integer id) {
        Record record = recordRepository.getOne(id);
        record.setOpen(!record.getOpen());
        recordRepository.save(record);
        List<Record> records = Lists.newArrayList();
        if(redisUtil.hasKey("open-record")){
            if(record.getOpen()){
                records = (List<Record>) redisUtil.getList("open-record",Record.class);
                records.add(0,record);
                records.sort(( Record ord1, Record ord2) -> ord2.getCreateTime().compareTo(ord1.getCreateTime()));
            }else{
                records.removeIf(r -> r.getId().equals(id));
            }
            redisUtil.setList("open-record", records,5*60);
        }
    }

    @Override
    public void delete(Integer id) {
        recordRepository.deleteById(id);
    }
}
