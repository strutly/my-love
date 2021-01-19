package com.love.strutly.service.impl;

import com.love.strutly.entity.Notice;
import com.love.strutly.repository.NoticeRepository;
import com.love.strutly.service.NoticeService;
import com.love.strutly.vo.req.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lj
 * @Date 2020/11/22 16:19
 * @Version 1.0
 */
@Service
public class NoticeServiceImpl implements NoticeService{

    @Autowired
    private NoticeRepository noticeRepository;

    @Override
    public Integer countByOid(Integer oid) {
        return noticeRepository.countByOid(oid);
    }

    @Override
    @Transactional
    public void deleteByOid(Integer oid) {
        noticeRepository.deleteByOid(oid);
    }

    @Override
    public List<Notice> page(PageVO vo) {
        List<Notice> notices = noticeRepository.findByOid(vo.getUid());
        return notices.stream().skip((vo.getPageNo())* vo.getPageSize()).limit(vo.getPageSize()).collect(Collectors.toList());
    }
}
