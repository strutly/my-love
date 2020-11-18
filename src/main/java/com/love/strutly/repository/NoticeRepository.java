package com.love.strutly.repository;

import com.love.strutly.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer>, JpaSpecificationExecutor<Notice> {

    /**
     * 根据 说说id 获取评论信息
     * @param rid
     * @return
     */
    List<Notice> findByRid(Integer rid);

    @Modifying
    @Query("delete from Notice a where a.id in (?1)")
    void deleteBatch(@Param(value = "ids") List<Integer> ids);
}
