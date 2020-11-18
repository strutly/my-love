package com.love.strutly.vo.resp.user;

import com.love.strutly.entity.Fans;
import lombok.Data;

import java.util.List;

/**
 * @Author lj
 * @Date 2020/11/15 9:09
 * @Version 1.0
 */
@Data
public class FansRespVO {
    List<Fans> fans;//关注我的粉丝
    List<Integer> toids;//我关注的id
}
