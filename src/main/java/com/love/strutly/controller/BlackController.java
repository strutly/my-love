package com.love.strutly.controller;

import com.google.common.collect.Lists;
import com.love.strutly.config.jwt.JwtUtil;
import com.love.strutly.service.MiniUserService;
import com.love.strutly.utils.DataResult;
import com.love.strutly.utils.HttpContextUtils;
import com.love.strutly.utils.RedisUtil;
import com.love.strutly.vo.resp.user.BlackRespVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lj
 * 小黑屋
 * @Date 2020/11/22 10:42
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "小程序模块--评论点赞分享管理")
@RequestMapping("/wxs")
public class BlackController {

    @Autowired
    private MiniUserService miniUserService;

    @Autowired
    private JwtUtil jwtUtil;

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/black")
    public DataResult<List<Integer>> getBlack(){
        String token = HttpContextUtils.getToken();
        String openid = jwtUtil.getClaim(token,"openid");
        List<Integer> ids = Lists.newArrayList();
        if(redisUtil.hasKey("black"+openid)){
            List<BlackRespVO> vos = Lists.newArrayList();
            vos = (List<BlackRespVO>) redisUtil.getList("black"+openid,BlackRespVO.class);
            vos = vos.stream().filter(vo -> vo.getTimestamp() > new Date().getTime())
                    .collect(Collectors.toList());
            redisUtil.setList("black"+openid, vos,-1);
            ids = vos.stream().map(BlackRespVO::getId).collect(Collectors.toList());
        }
        return DataResult.success(ids);
    }

    @PostMapping("/black/{id}")
    public DataResult black(@PathVariable(value = "id")Integer id){
        String token = HttpContextUtils.getToken();
        String openid = jwtUtil.getClaim(token,"openid");
        List<BlackRespVO> vos = Lists.newArrayList();
        if(redisUtil.hasKey("black"+openid)){
            vos = (List<BlackRespVO>) redisUtil.getList("black"+openid,BlackRespVO.class);
        }
        vos.add(new BlackRespVO(id));
        vos = vos.stream().filter(vo -> vo.getTimestamp() > new Date().getTime())
                .collect(Collectors.toList());
        redisUtil.setList("black"+openid, vos,-1);
        return DataResult.success();
    }
}
