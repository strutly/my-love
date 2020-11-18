package com.love.strutly;

import com.love.strutly.repository.CommentRepository;
import com.love.strutly.repository.FansRepository;
import com.love.strutly.service.FansService;
import com.love.strutly.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

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
    @Test
    void contextLoads() {

    }

}
