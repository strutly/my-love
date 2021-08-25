package com.love.strutly.vo.req;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @Author lj
 * @Date 2020/11/10 10:59
 * @Version 1.0
 */
@Data
public class PageVO {
    private Integer pageNo = 1;
    private Integer pageSize = 6;
    private Integer uid;
    private Boolean mine=false;
    private List<Integer> ids = Lists.newArrayList();
}
