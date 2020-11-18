package com.love.strutly.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author lj
 * 小程序用户表
 * @Date 2020/11/8 17:46
 * @Version 1.0
 */
@Data
@Entity
@Table(name = "ma_user")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class MiniUser extends BaseEntity{

    @JsonIgnore
    private String openId;

    private String nickName;

    private String gender;

    private String language;

    private String city;

    private String province;

    private String country;

    private String avatarUrl;

    @JsonIgnore
    private String unionId;
}
