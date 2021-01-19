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

    List<Notice> findByOid(@Param(value = "oid")Integer oid);

    Integer countByOid(@Param(value = "oid")Integer oid);

    @Modifying
    @Query("delete from Notice a where a.id in (?1)")
    void deleteBatch(@Param(value = "ids") List<Integer> ids);

    void deleteByOid(@Param(value = "oid")Integer oid);
}
