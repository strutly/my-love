package com.love.strutly.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.love.strutly.config.qiniu.ConstantQiniu;
import com.love.strutly.utils.DataResult;
import com.love.strutly.vo.resp.file.FileRespVO;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.UUID;

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
    /**
     * 图片base64 数据不附带头尾
     * @param json
     * @return
     */
    @PostMapping("/base64")
    public DataResult<FileRespVO> qiniuBase64(@RequestBody String json){
        DataResult<FileRespVO> result = DataResult.success();
        if(censor(json)){
            result = uploadBase64(json);
            return result;
        }
        return DataResult.fail("图片内容含有违法违规内容");
    }

    /**
     * 图片安全审核
     * @param base64
     * @return
     */
    private Boolean censor(String base64){
        String url = "http://ai.qiniuapi.com/v3/image/censor";
        String contentType = "application/json";
        String method = "POST";
        Auth auth = Auth.create(constantQiniu.getAccessKey(), constantQiniu.getSecretKey());
        String imageUrl = "data:application/octet-stream;base64,"+base64;
        String body = "{ \"data\": { \"uri\": \""+imageUrl+"\" }, \"params\": { \"scenes\": [ \"pulp\", \"terror\", \"politician\" ] } }";
        String qiniuToken = "Qiniu " + auth.signQiniuAuthorization(url, method, body.getBytes(), contentType);
        String host = "ai.qiniuapi.com";
        log.info("url={},body={},qiniuToken={}",url,body,qiniuToken);
        //头部部分
        StringMap header = new StringMap();
        header.put("Host",host);
        header.put("Authorization",qiniuToken);
        header.put("Content-Type", contentType);
        Configuration c = new Configuration(Region.huadong());
        Client client = new Client(c);
        try {
            Response response = client.post(url, body.getBytes(), header, contentType);
            log.info("response result={}",response.bodyString());
            JSONObject checkResult = JSON.parseObject(response.bodyString());
            JSONObject result = checkResult.getJSONObject("result");
            if(result.getString("suggestion").equals("pass")){
                return  true;
            }
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 将文件上传到七牛云
     */
    private DataResult<FileRespVO> uploadBase64(String base64) {
        DataResult<FileRespVO> result = DataResult.success();
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        //其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传
        try {
            Auth auth = Auth.create(constantQiniu.getAccessKey(), constantQiniu.getSecretKey());
            String upToken = auth.uploadToken(constantQiniu.getBucket());
            try {
                String fileRandomName = "mylove/"+UUID.randomUUID().toString();
                Response response = uploadManager.put(Base64.getDecoder().decode(base64), fileRandomName, upToken);
                // 解析上传成功的结果
                DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);

                String returnPath = constantQiniu.getPath() + "/" + putRet.key;

                FileRespVO fileRespVO = new FileRespVO();
                fileRespVO.setSrc(returnPath);
                fileRespVO.setName(fileRandomName);
                result.setData(fileRespVO);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
                result.setCode(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(-1);
        }
        return result;
    }




}
