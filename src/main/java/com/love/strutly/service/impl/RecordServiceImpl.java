package com.love.strutly.service.impl;

import com.google.common.collect.Lists;
import com.love.strutly.entity.Comment;
import com.love.strutly.entity.Record;
import com.love.strutly.filter.SensitiveFilter;
import com.love.strutly.repository.CommentRepository;
import com.love.strutly.repository.RecordRepository;
import com.love.strutly.service.RecordService;
import com.love.strutly.utils.BeanMapper;
import com.love.strutly.utils.DataResult;
import com.love.strutly.utils.RedisUtil;
import com.love.strutly.vo.req.PageVO;
import com.love.strutly.vo.req.RecordAddReqVO;
import com.love.strutly.vo.resp.record.RecordDetailRespVO;
import com.love.strutly.vo.resp.record.RecordListRespVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
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

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void save(RecordAddReqVO vo) {
        Record record = new Record();
        BeanMapper.mapExcludeNull(vo,record);
        if(StringUtils.isNotBlank(vo.getMsg())){
            vo.setMsg(sensitiveFilter.filter(vo.getMsg()));
        }
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
        Record record = recordRepository.findById(id).orElse(null);
        if(record!=null){
            recordDetailRespVO = BeanMapper.map(record,RecordDetailRespVO.class);
            List<Comment> records = commentRepository.findByRid(id);
            recordDetailRespVO.setCounts(records.stream().collect(Collectors.groupingBy(s -> s.getType())));
            return recordDetailRespVO;
        }
        return null;
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
        if(vo.getOpen()){
            records = recordRepository.findByMiniUser_IdOrderByCreateTimeDesc(vo.getUid());
        }else{
            records = recordRepository.findByMiniUser_IdAndOpenOrderByCreateTimeDesc(vo.getUid(),vo.getOpen());
        }
        List<Record> s = records.stream().skip((vo.getPageNo())* vo.getPageSize()).limit(vo.getPageSize()).collect(Collectors.toList());
        return BeanMapper.mapList(s,RecordListRespVO.class);
    }

    @Override
    public DataResult open(Integer id,Integer uid) {
        Record record = recordRepository.getOne(id);
        if(record!=null && !record.getMiniUser().getId().equals(uid)){
            return DataResult.fail("您没有权限进行此操作!");
        }
        record.setOpen(!record.getOpen());
        recordRepository.save(record);
        List<Record> records = Lists.newArrayList();
        if(redisUtil.hasKey("open-record")){
            records = (List<Record>) redisUtil.getList("open-record",Record.class);
            if(record.getOpen()){
                records.add(0,record);
            }else{
                records.removeIf(s -> s.getId().equals(record.getId()));
            }

            records = records.stream()
                    .sorted(Comparator.comparing(Record::getCreateTime).reversed())
                    .collect(Collectors.toList());

            redisUtil.setList("open-record", records,5*60);
        }
        return DataResult.success();
    }

    @Override
    public DataResult delete(Integer id, Integer uid) {
        Record record = recordRepository.getOne(id);
        if(record!=null && record.getMiniUser().getId().equals(uid)){
            recordRepository.deleteById(id);
            return DataResult.success();
        }
        return DataResult.fail("您没有权限进行此操作!");
    }
}
