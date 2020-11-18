package com.love.strutly.vo.resp.user;

import com.love.strutly.entity.MiniUser;
import com.love.strutly.vo.resp.record.CountRespVO;
import lombok.Data;

/**
 * @Author lj
 * @Date 2020/11/12 17:09
 * @Version 1.0
 */
@Data
public class MiniUserRespVO {
    private MiniUser miniUser;
    private CountRespVO counts;
    private Boolean mine = false;
    private Boolean follow = false;
}
