package com.love.strutly.repository;

import com.love.strutly.entity.Fans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FansRepository extends JpaRepository<Fans, Integer>, JpaSpecificationExecutor<Fans> {

    /**
     * 根据用户id获取所有关注对象id
     * @param uid
     * @return
     */
    @Query(value = "select oid from love_fans where uid=?1",nativeQuery = true)
    List<Integer> findByUid(@Param(value="uid")Integer uid);


    Fans findByMine_IdAndAndTo_Id(@Param(value = "uid")Integer uid,@Param(value="oid")Integer oid);

    /**
     * oid 获取所以粉丝
     * @param oid
     * @return
     */
    List<Fans> findByTo_id(Integer oid);


    /**
     * 获取我关注的所有对象
     * @param uid
     * @return
     */
    List<Fans> findByMine_Id(Integer uid);
    @Modifying
    @Query("delete from Fans a where a.id in (?1)")
    void deleteBatch(@Param(value = "ids") List<Integer> ids);
}
