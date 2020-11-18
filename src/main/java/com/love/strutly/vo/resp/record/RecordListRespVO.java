package com.love.strutly.vo.resp.record;

import com.love.strutly.entity.MiniUser;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author lj
 * @Date 2020/11/10 10:54
 * @Version 1.0
 */
@Data
public class RecordListRespVO {
    private Integer id;

    private MiniUser miniUser;

    private String msg;//内容

    private List<String> imgs;

    private Date createTime;

    private Boolean open;
}
