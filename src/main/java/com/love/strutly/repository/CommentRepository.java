package com.love.strutly.repository;

import com.love.strutly.entity.Comment;
import com.love.strutly.vo.resp.record.CountRespVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {

    /**
     * 根据 说说id 获取评论信息
     * @param rid
     * @return
     */
    List<Comment> findByRid(Integer rid);


    List<Comment> findByUidAndTypeOrderByIdDesc(@Param(value ="uid") Integer uid,@Param(value ="type") Integer type);

    Comment findByRidAndTypeAndUid(@Param(value ="rid") Integer rid,@Param(value ="type") Integer type,@Param(value ="uid") Integer uid);

    @Modifying
    @Query("delete from Comment a where a.id in (?1)")
    void deleteBatch(@Param(value = "ids") List<Integer> ids);

    @Query(value = "select sum(case when type=1 then 1 else 0 end) as sum1,sum(case when type=2 then 1 else 0 end) as sum2,sum(case when type=3 then 1 else 0 end) as sum3 from love_comment where rid = ?1",nativeQuery = true)
    CountRespVO countByRid(@Param(value ="rid") Integer rid);
}
