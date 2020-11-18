package com.love.strutly.vo.req;

import lombok.Data;

/**
 * @Author lj
 * @Date 2020/11/10 10:59
 * @Version 1.0
 */
@Data
public class PageVO {
    private Integer pageNo = 0;
    private Integer pageSize = 6;
    private Integer uid;
    private Boolean mine=false;
}
