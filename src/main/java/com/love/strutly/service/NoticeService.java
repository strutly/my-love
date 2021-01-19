package com.love.strutly.service;

import com.love.strutly.entity.Notice;
import com.love.strutly.vo.req.PageVO;

import java.util.List;

/**
 * @Author lj
 * @Date 2020/11/22 16:19
 * @Version 1.0
 */
public interface NoticeService {
    Integer countByOid(Integer oid);
    void deleteByOid(Integer oid);

    List<Notice> page(PageVO vo);
}
