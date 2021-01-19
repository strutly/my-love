package com.love.strutly.vo.resp.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author lj
 * @Date 2020/11/22 11:22
 * @Version 1.0
 */
@Data
public class BlackRespVO implements Serializable{
    private Integer id;
    private Long timestamp;

    public BlackRespVO(){

    }
    public BlackRespVO(Integer id){
        this.id = id;
        this.timestamp = new Date().getTime()+7*24*60*60L;
    }
}
