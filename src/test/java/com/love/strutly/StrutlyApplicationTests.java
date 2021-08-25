package com.love.strutly;

import com.love.strutly.filter.SensitiveFilter;
import com.love.strutly.repository.CommentRepository;
import com.love.strutly.repository.FansRepository;
import com.love.strutly.service.FansService;
import com.love.strutly.service.RecordService;
import com.love.strutly.utils.RedisUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
@SpringBootTest
class StrutlyApplicationTests implements Serializable  {
    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private FansRepository fansRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FansService fansService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Test
    @Rollback(false)
    @Transactional
    void contextLoads() throws WxErrorException {

        //recordService.saveAll();
    }

}
