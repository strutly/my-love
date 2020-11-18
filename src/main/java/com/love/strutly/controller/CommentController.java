package com.love.strutly.controller;

import com.love.strutly.config.jwt.JwtUtil;
import com.love.strutly.entity.MiniUser;
import com.love.strutly.service.CommentService;
import com.love.strutly.service.MiniUserService;
import com.love.strutly.utils.DataResult;
import com.love.strutly.utils.HttpContextUtils;
import com.love.strutly.vo.req.CommentAddReqVO;
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
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MiniUserService miniUserService;

    @PostMapping("comment")
    public DataResult form(@RequestBody @Valid CommentAddReqVO vo){
        System.out.println(vo.toString());
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        vo.setUid(miniUser.getId());
        vo.setAvatarUrl(miniUser.getAvatarUrl());
        vo.setNickName(miniUser.getNickName());
        if(miniUser.getId().equals(vo.getOid())){
            vo.setOid(null);
            vo.setOtherName(null);
        }
        commentService.save(vo);
        return DataResult.success();
    }

    @GetMapping("comment/{id}")
    public DataResult like(@PathVariable("id")Integer id){
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        System.out.println(id);
        System.out.println(miniUser.getId());
        return DataResult.success(commentService.like(id,miniUser.getId()));
    }
}
