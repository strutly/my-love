package com.love.strutly.api;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.love.strutly.config.jwt.JwtUtil;
import com.love.strutly.config.mini.WxMaConfiguration;
import com.love.strutly.entity.MiniUser;
import com.love.strutly.service.MiniUserService;
import com.love.strutly.utils.DataResult;
import com.love.strutly.vo.req.WxLoginReqVO;
import com.love.strutly.vo.resp.user.TokenRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Slf4j
@RestController
@Api(tags = "小程序接口")
@RequestMapping("/api")
public class MiniAuthController {

    @Autowired
    private MiniUserService miniUserService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 登录接口
     */
    @GetMapping("/login")
    @ApiOperation(value = "用户登录接口")
    public DataResult<TokenRespVO> login(String code) {
        if (StringUtils.isBlank(code)) {
            return DataResult.fail("授权信息不全,请重新进行授权!");
        }
        final WxMaService wxService = WxMaConfiguration.getMaService();
        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            log.info(session.toString());
            MiniUser miniUser = miniUserService.findByOpenId(session.getOpenid());
            if(null != miniUser){
                return DataResult.success(jwtUtil.getToken(miniUser));
            }else{
                return DataResult.fail("登录失败!");
            }
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return DataResult.fail("登录失败!");
        }
    }

    /**
     * 重新获取token
     * @param refreshToken
     * @return
     */
    @GetMapping("/refresh")
    @ApiOperation(value = "刷新用户token")
    public DataResult refresh(@RequestParam("refreshToken") String refreshToken){
        // 执行认证
        if (refreshToken == null) {
            return DataResult.fail("获取失败!");
        }
        // 获取 token 中的 user id
        if(!jwtUtil.verify(refreshToken)){
            return DataResult.fail("token 认证失败!");
        }
        String openid = jwtUtil.getClaim(refreshToken,"openid");
        MiniUser miniUser = miniUserService.findByOpenId(openid);
        return DataResult.success(jwtUtil.getToken(miniUser));
    }


    /**
     * 授权 获取身份信息
     * @param wxLoginVo
     * @return
     */
    @PostMapping("/auth")
    @ApiOperation(value = "用户授权接口")
    public DataResult<TokenRespVO> sign(@RequestBody WxLoginReqVO wxLoginVo) throws JsonProcessingException {
        final WxMaService wxService = WxMaConfiguration.getMaService();
        log.info("登录信息为:{}",wxLoginVo);
        String code = wxLoginVo.getCode();
        if(StringUtils.isBlank(code)){
            return DataResult.unAuth("授权信息不全,请重新进行授权");
        }
        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);

            if (!wxService.getUserService().checkUserInfo(session.getSessionKey(), wxLoginVo.getRawData(), wxLoginVo.getSignature())) {
                return DataResult.unAuth("user check failed");
            }
            WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(session.getSessionKey(), wxLoginVo.getEncryptedData(), wxLoginVo.getIv());
            userInfo.setUnionId(session.getUnionid());
            userInfo.setOpenId(session.getOpenid());
            MiniUser miniUser = miniUserService.save(userInfo);
            if(null != miniUser){
                return DataResult.success(jwtUtil.getToken(miniUser));
            }else{
                return DataResult.unAuth("登录失败!");
            }
        }catch (WxErrorException e) {
            return DataResult.unAuth("授权失败,请稍后再试!");
        }
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @GetMapping("/phone")
    public DataResult phone(String sessionKey, String signature,
                        String rawData, String encryptedData, String iv) {
        DataResult result = DataResult.success();
        final WxMaService wxService = WxMaConfiguration.getMaService();
        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return result;
        }

        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);

        return result;
    }
}