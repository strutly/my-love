package com.love.strutly.vo.resp.record;

import com.love.strutly.entity.Comment;
import com.love.strutly.entity.MiniUser;
import com.love.strutly.entity.Record;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author lj
 * @Date 2020/11/9 16:22
 * @Version 1.0
 */
@Data
public class RecordDetailRespVO {
    private MiniUser miniUser;

    private String msg;//内容

    private Boolean open;

    private List<Record.MediaVO> imgs;

    private Date createTime;

    private Map<Integer,List<Comment>> counts;

}
