package com.love.strutly.vo.resp.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author lj
 * @Date 2020/11/13 15:57
 * @Version 1.0
 */
@Data
public class TokenRespVO {
    @ApiModelProperty("uid")
    private Integer id;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("token过期时间")
    private Long expireTime;

    @ApiModelProperty("refreshToken,可用来刷新token")
    private String refreshToken;

    @ApiModelProperty("refreshToken过期时间")
    private Long refreshExpireTime;

    public TokenRespVO(Integer id,String token, Long expireTime, String refreshToken, Long refreshExpireTime){
        this.id = id;
        this.refreshToken = refreshToken;
        this.token = token;
        this.expireTime = expireTime;
        this.refreshExpireTime = refreshExpireTime;
    };

}
