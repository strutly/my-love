package com.love.strutly.controller;

import cn.binarywang.wx.miniapp.api.WxMaSecCheckService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import com.google.common.collect.Lists;
import com.love.strutly.aop.annotation.PassToken;
import com.love.strutly.config.jwt.JwtUtil;
import com.love.strutly.config.mini.WxMaConfiguration;
import com.love.strutly.entity.MiniUser;
import com.love.strutly.service.MiniUserService;
import com.love.strutly.service.RecordService;
import com.love.strutly.utils.DataResult;
import com.love.strutly.utils.HttpContextUtils;
import com.love.strutly.utils.RedisUtil;
import com.love.strutly.vo.req.PageVO;
import com.love.strutly.vo.req.RecordAddReqVO;
import com.love.strutly.vo.resp.record.RecordDetailRespVO;
import com.love.strutly.vo.resp.user.BlackRespVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private RedisUtil redisUtil;

    @PassToken
    @GetMapping("/record")
    public DataResult list(PageVO vo){
        String token = HttpContextUtils.getToken();
        List<Integer> ids = Lists.newArrayList();
        if(StringUtils.isNotBlank(token)){
            String openid = jwtUtil.getClaim(token,"openid");
            if(redisUtil.hasKey("black"+openid)){
                List<BlackRespVO> vos = Lists.newArrayList();
                vos = (List<BlackRespVO>) redisUtil.getList("black"+openid,BlackRespVO.class);
                vos = vos.stream().filter(v -> v.getTimestamp() > new Date().getTime())
                        .collect(Collectors.toList());
                redisUtil.setList("black"+openid, vos,-1);
                ids = vos.stream().map(BlackRespVO::getId).collect(Collectors.toList());
            }
            vo.setIds(ids);
        }

        return DataResult.success(recordService.list(vo));
    }

    @GetMapping("/record/my")
    public DataResult myList(PageVO vo){
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        if(miniUser.getId().equals(vo.getUid())){
            vo.setMine(true);
        }
        return DataResult.success(recordService.list(vo));
    }

    @PassToken
    @GetMapping("/record/{id}")
    public DataResult detail(@PathVariable("id")Integer id){
        RecordDetailRespVO vo = recordService.getOne(id);
        if(vo!=null && !vo.getOpen()){
            String token = HttpContextUtils.getToken();
            MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
            if(!vo.getMiniUser().getId().equals(miniUser.getId())){
                return DataResult.getResult(0,"该内容不存在,去主页看看吧!");
            }
        }
        return DataResult.success(vo);
    }

    @PostMapping("/record")
    public DataResult form(@RequestBody @Valid RecordAddReqVO vo){
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        vo.setMiniUser(miniUser);
        final WxMaService wxService = WxMaConfiguration.getMaService();
        WxMaSecCheckService wxMaSecCheckService = wxService.getSecCheckService();
        String msg = vo.getMsg();
        try {
            if(wxMaSecCheckService.checkMessage(msg)){
                recordService.save(vo);
                return DataResult.success();
            }else{
                return DataResult.fail("您发布的内容含有违法违规内容");
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            return DataResult.fail("您发布的内容含有违法违规内容");
        }
    }

    @PutMapping("record/{id}")
    public DataResult open(@PathVariable("id")Integer id){
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        return recordService.open(id,miniUser.getId());
    }

    @DeleteMapping("record/{id}")
    public DataResult delete(@PathVariable("id")Integer id){
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        return recordService.delete(id,miniUser.getId());
    }



}
