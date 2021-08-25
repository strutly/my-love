package com.love.strutly.service.impl;

import com.love.strutly.entity.Comment;
import com.love.strutly.entity.MiniUser;
import com.love.strutly.entity.Record;
import com.love.strutly.exception.code.BaseExceptionType;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
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
        recordRepository.save(record);
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

    /*@Override
    public void saveAll() {
        List<Record> records = recordRepository.findAll();
        records.forEach(record -> {
            List<Record.MediaVO> vos = Lists.newArrayList();
            List<String> imgs = record.getImgs();
            for (int i = 0; i <imgs.size() ; i++) {
                Record.MediaVO mediaVO = new Record.MediaVO();
                mediaVO.setCover(imgs.get(i));
                mediaVO.setType(0);
                mediaVO.setUrl(imgs.get(i));
                vos.add(mediaVO);
            }
            record.setImgs1(vos);
            recordRepository.save(record);
        });
    }*/

    @Override
    public Page<Record> list(PageVO vo) {
        Pageable pageable = PageRequest.of(vo.getPageNo()-1, vo.getPageSize(), Sort.Direction.DESC,"id");
        return recordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            //查询and
            List<Predicate> listAnd = new ArrayList<Predicate>();
            Join<Object, MiniUser> miniUser = root.join("miniUser", JoinType.LEFT);
            /**
             * 不是自己时,只显示开放和审核通过的
             */
            if(vo.getUid()!=null){
                listAnd.add(criteriaBuilder.equal(miniUser.get("id"),vo.getUid()));
            }


            if (!vo.getMine()){
                listAnd.add(criteriaBuilder.equal(root.get("open"),true));
                listAnd.add(criteriaBuilder.equal(root.get("status"),true));
            }
            /**
             * 黑名单的暂时过滤
             */
            if(!vo.getIds().isEmpty()){
                listAnd.add(criteriaBuilder.not(criteriaBuilder.in(miniUser.get("id")).value(vo.getIds())));
            }
            return criteriaQuery.where(listAnd.toArray(new Predicate[listAnd.size()])).getRestriction();
        },pageable);
    }

    @Override
    public DataResult open(Integer id,Integer uid) {
        Record record = recordRepository.getOne(id);
        if(record!=null && !record.getMiniUser().getId().equals(uid)){
            return DataResult.getResult(BaseExceptionType.USER_ERROR.getCode(),"您没有权限进行此操作!");
        }
        record.setOpen(!record.getOpen());
        recordRepository.save(record);
        return DataResult.success();
    }

    @Override
    public DataResult delete(Integer id, Integer uid) {
        Record record = recordRepository.getOne(id);
        if(record!=null && record.getMiniUser().getId().equals(uid)){
            recordRepository.deleteById(id);
            return DataResult.success();
        }
        return DataResult.getResult(BaseExceptionType.USER_ERROR.getCode(),"您没有权限进行此操作!");
    }
}
