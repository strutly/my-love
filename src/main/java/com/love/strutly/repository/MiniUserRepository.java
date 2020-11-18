package com.love.strutly.repository;

import com.love.strutly.entity.MiniUser;
import com.love.strutly.vo.resp.record.CountRespVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MiniUserRepository extends JpaRepository<MiniUser, Integer>, JpaSpecificationExecutor<MiniUser> {

    /**
     * 根据 openId 获取用户信息
     * @param openId
     * @return
     */
    MiniUser findByOpenId(String openId);

    @Modifying
    @Query("delete from MiniUser a where a.id in (?1)")
    void deleteBatch(@Param(value = "ids") List<Integer> ids);

    @Query(value = "select * from  (select sum(case when oid=?1 then 1 else 0 end) as sum1,sum(case when uid=?1 then 1 else 0 end) as sum2 from love_fans) as t1 , (select count(*) as sum3 from love_comment where uid=?1 and type=2) as t2",nativeQuery = true)
    CountRespVO countById(@Param(value = "id") Integer id);
}
