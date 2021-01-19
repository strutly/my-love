package com.love.strutly;

import com.love.strutly.filter.SensitiveFilter;
import com.love.strutly.repository.CommentRepository;
import com.love.strutly.repository.FansRepository;
import com.love.strutly.service.FansService;
import com.love.strutly.utils.RedisUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
class StrutlyApplicationTests {
    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private FansRepository fansRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FansService fansService;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Test
    void contextLoads() throws WxErrorException {
        String key = "性交";
        Long t = new Date().getTime();
        key = sensitiveFilter.filter(key);
        System.out.println(new Date().getTime()-t);
        System.out.println(key);
    }

}
