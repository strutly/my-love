package com.love.strutly.service;

import com.love.strutly.vo.req.FollowReqVO;
import com.love.strutly.vo.resp.user.FansRespVO;

public interface FansService {
    void follow(FollowReqVO vo);
    Boolean fans(Integer uid, Integer oid);

    FansRespVO getAllFans(Integer uid);

    FansRespVO getMeFollow(Integer uid);
}
