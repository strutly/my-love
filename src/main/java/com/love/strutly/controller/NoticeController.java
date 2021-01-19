package com.love.strutly.controller;

import com.love.strutly.config.jwt.JwtUtil;
import com.love.strutly.entity.MiniUser;
import com.love.strutly.service.MiniUserService;
import com.love.strutly.service.NoticeService;
import com.love.strutly.utils.DataResult;
import com.love.strutly.utils.HttpContextUtils;
import com.love.strutly.vo.req.PageVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lj
 * @Date 2020/11/22 16:15
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "小程序模块--评论点赞分享管理")
@RequestMapping("/wxs")
public class NoticeController {
    @Autowired
    private MiniUserService miniUserService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("notice/num")
    public DataResult num(){
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        return DataResult.success(noticeService.countByOid(miniUser.getId()));
    }

    @GetMapping("notice")
    public DataResult list(PageVO vo){
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        vo.setUid(miniUser.getId());
        return DataResult.success(noticeService.page(vo));
    }


    @DeleteMapping("notice")
    public DataResult delete(){
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        noticeService.deleteByOid(miniUser.getId());
        return DataResult.success();
    }


}
