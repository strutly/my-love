package com.love.strutly.controller;

import com.love.strutly.config.jwt.JwtUtil;
import com.love.strutly.entity.MiniUser;
import com.love.strutly.service.FansService;
import com.love.strutly.service.MiniUserService;
import com.love.strutly.utils.DataResult;
import com.love.strutly.utils.HttpContextUtils;
import com.love.strutly.vo.req.FollowReqVO;
import com.love.strutly.vo.resp.user.FansRespVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author lj
 * @Date 2020/11/11 14:46
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "小程序模块--评论点赞分享管理")
@RequestMapping("/wxs")
public class FansController {
    @Autowired
    private FansService fansService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MiniUserService miniUserService;

    @PostMapping("fans")
    public DataResult form(@RequestBody @Valid FollowReqVO vo){
        System.out.println(vo);
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        vo.setUid(miniUser.getId());
        fansService.follow(vo);
        return DataResult.success();
    }

    /**
     * 获取我的粉丝以及我的关注ids
     * @return
     */
    @GetMapping("fans")
    public DataResult<FansRespVO> fans(){
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        return DataResult.success(fansService.getAllFans(miniUser.getId()));
    }

    /**
     * 获取我的关注
     */
    @GetMapping("fans/me")
    public DataResult<FansRespVO> meFollow(){
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        return DataResult.success(fansService.getMeFollow(miniUser.getId()));
    }

}
