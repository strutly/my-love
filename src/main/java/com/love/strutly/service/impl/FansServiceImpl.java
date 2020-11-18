package com.love.strutly.service.impl;

import com.love.strutly.entity.Fans;
import com.love.strutly.entity.MiniUser;
import com.love.strutly.repository.FansRepository;
import com.love.strutly.service.FansService;
import com.love.strutly.vo.req.FollowReqVO;
import com.love.strutly.vo.resp.user.FansRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author lj
 * @Date 2020/11/13 20:17
 * @Version 1.0
 */
@Service
public class FansServiceImpl implements FansService{

    @Autowired
    private FansRepository fansRepository;

    @Override
    public void follow(FollowReqVO vo) {
        Fans fans = fansRepository.findByMine_IdAndAndTo_Id(vo.getUid(),vo.getOid());
        if(fans==null){
            fans = new Fans();

            MiniUser mine = new MiniUser();
            mine.setId(vo.getUid());
            fans.setMine(mine);

            MiniUser to = new MiniUser();
            to.setId(vo.getOid());
            fans.setTo(to);

            fansRepository.save(fans);
        }else{
            fansRepository.delete(fans);
        }
    }

    @Override
    public Boolean fans(Integer uid, Integer oid) {
        return fansRepository.findByMine_IdAndAndTo_Id(uid,oid)==null?false:true;
    }

    @Override
    public FansRespVO getAllFans(Integer uid) {
        FansRespVO fansRespVO = new FansRespVO();
        fansRespVO.setFans(fansRepository.findByTo_id(uid));
        fansRespVO.setToids(fansRepository.findByUid(uid));
        return fansRespVO;
    }

    @Override
    public FansRespVO getMeFollow(Integer uid) {
        FansRespVO fansRespVO = new FansRespVO();
        fansRespVO.setFans(fansRepository.findByMine_Id(uid));
        return fansRespVO;
    }
}
