package com.love.strutly.controller;

import com.love.strutly.aop.annotation.PassToken;
import com.love.strutly.config.jwt.JwtUtil;
import com.love.strutly.entity.MiniUser;
import com.love.strutly.service.MiniUserService;
import com.love.strutly.service.RecordService;
import com.love.strutly.utils.DataResult;
import com.love.strutly.utils.HttpContextUtils;
import com.love.strutly.vo.req.PageVO;
import com.love.strutly.vo.req.RecordAddReqVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author lj
 * @Date 2020/11/9 15:56
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "小程序模块--说说管理")
@RequestMapping("/wxs")
public class RecordController {

    @Autowired
    private MiniUserService miniUserService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private JwtUtil jwtUtil;


    @PassToken
    @GetMapping("/record")
    public DataResult list(PageVO vo){
        return DataResult.success(recordService.page(vo));
    }

    @GetMapping("/record/my")
    public DataResult myList(PageVO vo){
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        if(miniUser.getId().equals(vo.getUid())){
            vo.setMine(true);
            vo.setUid(miniUser.getId());
        }
        return DataResult.success(recordService.myList(vo));
    }


    @PassToken
    @GetMapping("/record/{id}")
    public DataResult detail(@PathVariable("id")Integer id){
        return DataResult.success(recordService.getOne(id));
    }

    @PostMapping("/record")
    public DataResult form(@RequestBody @Valid RecordAddReqVO vo){
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        vo.setMiniUser(miniUser);
        recordService.save(vo);
        return DataResult.success();
    }


    @PutMapping("record/{id}")
    public DataResult open(@PathVariable("id")Integer id){
        recordService.open(id);
        return DataResult.success();
    }

    @DeleteMapping("record/{id}")
    public DataResult delete(@PathVariable("id")Integer id){
        return DataResult.success(recordService.getOne(id));
    }


}
