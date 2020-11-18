package com.love.strutly.repository;

import com.love.strutly.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer>, JpaSpecificationExecutor<Record> {

    /**
     * 根据 说说id 获取说说
     * @param uid
     * @return
     */
    List<Record> findByMiniUser_IdOrderByCreateTimeDesc(Integer uid);


    /**
     * @return
     */
    List<Record> findByOpenIsTrueOrderByCreateTimeDesc();

    List<Record> findByOpenIsTrueAndMiniUser_IdOrderByCreateTimeDesc(@Param(value = "uid")Integer uid);

    @Modifying
    @Query("delete from Record a where a.id in (?1)")
    void deleteBatch(@Param(value = "ids") List<Integer> ids);
}
