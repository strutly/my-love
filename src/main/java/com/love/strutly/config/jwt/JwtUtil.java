package com.love.strutly.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.love.strutly.entity.MiniUser;
import com.love.strutly.exception.BusinessException;
import com.love.strutly.exception.code.BaseExceptionType;
import com.love.strutly.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service("JwtUtil")
public class JwtUtil {
 
    @Autowired
    private JwtProperties jwtProperties;

    @Resource
    private RedisUtil redisUtil;


    public String getToken(MiniUser miniUser) {

        if(redisUtil.hasKey("user-token-"+miniUser.getOpenId())){
            return (String) redisUtil.get("user-token-"+miniUser.getOpenId());
        }else{
            log.info("公众号或小程序--没有缓存!");
            Algorithm algorithm = Algorithm.HMAC256(jwtProperties.secretKey);
            Date exp = new Date(System.currentTimeMillis() + jwtProperties.getTokenExpireTime()*60*1000l);
            // 头部信息
            Map<String, Object> header = new HashMap<String, Object>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");
            String token = JWT.create()
                    .withHeader(header)//设置头部信息 Header
                    .withClaim("openid",miniUser.getOpenId())
                    .withClaim("id",miniUser.getId().toString())
                    .withExpiresAt(exp)//设置 载荷 签名过期的时间
                    .sign(algorithm);//签名 Signature
            Boolean flag = redisUtil.set("user-token-"+miniUser.getOpenId(),token,60);
            System.out.println(flag);
            return token;
        }
    }

    public boolean verify(String token){
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.secretKey);
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getClaim(String token, String claim) {
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.secretKey);
        try{
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaims().get(claim).asString();
        }catch (IllegalArgumentException e) {
            throw new BusinessException(BaseExceptionType.TOKEN_ERROR,"token认证失败1");
        }catch (JWTVerificationException e) {
            throw new BusinessException(BaseExceptionType.TOKEN_ERROR,"token认证失败2");
        }catch (Exception e){
            log.error(e.toString());
            log.error(e.getMessage());
            throw new BusinessException(BaseExceptionType.TOKEN_ERROR,"token认证失败3");
        }
    }
}