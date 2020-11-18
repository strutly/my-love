package com.love.strutly.api;

import com.love.strutly.config.qiniu.ConstantQiniu;
import com.love.strutly.utils.DataResult;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lj
 * @Date 2020/11/11 17:10
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "小程序接口")
@RequestMapping("/api/qiniu")
public class QiNiuController {
    @Autowired
    private ConstantQiniu constantQiniu;
    @GetMapping("token")
    public DataResult<String> token(){
        Auth auth = Auth.create(constantQiniu.getAccessKey(), constantQiniu.getSecretKey());
        String upToken = auth.uploadToken(constantQiniu.getBucket());
        return DataResult.success(upToken);
    }

}
