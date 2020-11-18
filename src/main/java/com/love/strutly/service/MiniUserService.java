package com.love.strutly.service;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.love.strutly.entity.MiniUser;
import com.love.strutly.vo.resp.record.CountRespVO;

/**
 * @Author lj
 * @Date 2020/11/9 11:53
 * @Version 1.0
 */
public interface MiniUserService {
    MiniUser findByOpenId(String openId);
    MiniUser save(WxMaUserInfo wxMaUserInfo);
    CountRespVO countById(Integer id);
    MiniUser getOne(Integer id);
}
