package com.love.strutly.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.love.strutly.entity.MiniUser;
import com.love.strutly.repository.MiniUserRepository;
import com.love.strutly.service.MiniUserService;
import com.love.strutly.utils.BeanMapper;
import com.love.strutly.vo.resp.record.CountRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author lj
 * @Date 2020/11/9 11:53
 * @Version 1.0
 */
@Service
public class MiniUserServiceImpl implements MiniUserService{

    @Autowired
    private MiniUserRepository miniUserRepository;

    @Override
    public MiniUser findByOpenId(String openId) {
        return miniUserRepository.findByOpenId(openId);
    }

    @Override
    public MiniUser save(WxMaUserInfo wxMaUserInfo) {
        MiniUser miniUser = findByOpenId(wxMaUserInfo.getOpenId());
        if(miniUser==null)
            miniUser = new MiniUser();
        BeanMapper.mapExcludeNull(wxMaUserInfo,miniUser);
        return miniUserRepository.save(miniUser);
    }

    @Override
    public CountRespVO countById(Integer id) {
        return miniUserRepository.countById(id);
    }

    @Override
    public MiniUser getOne(Integer id) {
        MiniUser miniUser = miniUserRepository.getOne(id);
        System.out.println(id);
        System.out.println(miniUser.toString());
        return miniUser;
    }
}
