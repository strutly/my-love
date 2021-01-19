package com.love.strutly.vo.req;

import lombok.Data;

/**
 * @Author lj
 * @Date 2020/11/22 20:51
 * @Version 1.0
 */
@Data
public class UserSyncReqVO {
    private String openId;
    private String nickName;
    private String gender;
    private String language;
    private String city;
    private String province;
    private String country;
    private String avatarUrl;
}
