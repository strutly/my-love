package com.love.strutly.controller;

import cn.binarywang.wx.miniapp.api.WxMaSecCheckService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import com.love.strutly.config.jwt.JwtUtil;
import com.love.strutly.config.mini.WxMaConfiguration;
import com.love.strutly.entity.MiniUser;
import com.love.strutly.service.CommentService;
import com.love.strutly.service.MiniUserService;
import com.love.strutly.utils.DataResult;
import com.love.strutly.utils.HttpContextUtils;
import com.love.strutly.vo.req.CommentAddReqVO;
import com.love.strutly.vo.req.PageVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
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


    @GetMapping("comment")
    public DataResult list(PageVO vo){
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        vo.setUid(miniUser.getId());
        return DataResult.success(commentService.page(vo));
    }

    @PostMapping("comment")
    public DataResult form(@RequestBody @Valid CommentAddReqVO vo){
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        vo.setUid(miniUser.getId());
        vo.setAvatarUrl(miniUser.getAvatarUrl());
        vo.setNickName(miniUser.getNickName());
        final WxMaService wxService = WxMaConfiguration.getMaService();
        WxMaSecCheckService wxMaSecCheckService = wxService.getSecCheckService();
        if(StringUtils.isNotBlank(vo.getContent())){
            try {
                if(!wxMaSecCheckService.checkMessage(vo.getContent())){
                    return DataResult.fail("您发布的内容含有违法违规内容");
                }
            } catch (WxErrorException e) {
                e.printStackTrace();
                return DataResult.fail("您发布的内容含有违法违规内容");
            }
        }
        return DataResult.success(commentService.save(vo));
    }

    @GetMapping("comment/{id}")
    public DataResult like(@PathVariable("id")Integer id){
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        return DataResult.success(commentService.like(id,miniUser.getId()));
    }

    @DeleteMapping("comment/{id}")
    public DataResult delete(@PathVariable("id")Integer id){
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        return commentService.delete(id,miniUser.getId());
    }

}
