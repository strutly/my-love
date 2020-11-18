package com.love.strutly.controller;

import com.love.strutly.config.jwt.JwtUtil;
import com.love.strutly.entity.MiniUser;
import com.love.strutly.service.FansService;
import com.love.strutly.service.MiniUserService;
import com.love.strutly.utils.DataResult;
import com.love.strutly.utils.HttpContextUtils;
import com.love.strutly.vo.resp.user.MiniUserRespVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lj
 * @Date 2020/11/9 15:56
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "小程序模块--说说管理")
@RequestMapping("/wxs")
public class MiniUserController {

    @Autowired
    private MiniUserService miniUserService;

    @Autowired
    private FansService fansService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/user/{id}")
    public DataResult<MiniUserRespVO> my_list(@PathVariable("id")Integer id){
        MiniUserRespVO miniUserRespVO = new MiniUserRespVO();
        String token = HttpContextUtils.getToken();
        MiniUser miniUser = miniUserService.findByOpenId(jwtUtil.getClaim(token,"openid"));
        if(miniUser.getId().equals(id)){
            miniUserRespVO.setMine(true);
            miniUserRespVO.setCounts(miniUserService.countById(id));
        }else{
            miniUserRespVO.setFollow(fansService.fans(miniUser.getId(),id));
            miniUser = miniUserService.getOne(id);
        }
        miniUserRespVO.setMiniUser(miniUser);
        return DataResult.success(miniUserRespVO);
    }

}
