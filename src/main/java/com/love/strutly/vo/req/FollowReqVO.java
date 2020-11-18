package com.love.strutly.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author lj
 * @Date 2020/11/13 20:16
 * @Version 1.0
 */
@Data
public class FollowReqVO {
    @ApiModelProperty(value = "关注对象id",name="oid")
    @NotNull(message = "关注对象不能为空")
    private Integer oid;
    private Integer uid;
}
