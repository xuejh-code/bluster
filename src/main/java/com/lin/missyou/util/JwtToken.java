package com.lin.missyou.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtToken {
    private static String jwtKey;
    private static Integer expiredTimeIn;
    private static Integer defaultScope = 8;
    @Value("${missyou.security.jwt-key}")
    public void setJwtKey(String jwtKey){
        JwtToken.jwtKey = jwtKey;
    }
    @Value(("${missyou.security.token-expired-in}"))
    public void setExpiredTimeIn(Integer expiredTimeIn){
        JwtToken.expiredTimeIn = expiredTimeIn;
    }

    public static Optional<Map<String, Claim>> getClaims(String token){
        DecodedJWT decodedJWT;
        Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try{
            decodedJWT = jwtVerifier.verify(token);
        } catch (JWTVerificationException e){
            //这里的异常不建议记录日志，别人访问接口都会带令牌，会造成日志的大量积累；
            //不要记录日志，也不要打印，不是程序的问题，用户输入的问题通常都不建议记录日志；
            //返回空，表示令牌校验失败；
            return Optional.empty();
        }
        return Optional.of(decodedJWT.getClaims());
    }

    //makeToken需要接受一些参数，然后将这些参数写到Jwt令牌里面；
    //判断某个用户是否有访问某个接口的权限就是拿jwt令牌里的scope和接口的scope进行比较；
    public static String makeToken(Long uid,Integer scope){
        return JwtToken.getToken(uid,scope);
    }
    public static String makeToken(Long uid){
        return JwtToken.getToken(uid,JwtToken.defaultScope);
    }
    //私有的方法写全部生成jwt令牌的逻辑；
    private static String getToken(Long uid,Integer scope){
        //如何生成JWT令牌,并把两个参数写到令牌里去；
        //自己写生成jwt的代码也可以，但是有比较好用的库，jjwt和auth0，推荐auth0有很多安全方面的东西，并且库会更好用一点；
        //java的jwt库会比其他语言的更复杂一点，其他语言基本直接调用一个方法就可以生成令牌了；
        //需要了解一些jwt的技术名词
        //生成令牌的过程，还有一个验证令牌的过程，验证过程也并不简洁；
        Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey);
        Map<String,Date> map = JwtToken.calculateExpiredIssues();
        return JWT.create()
                .withClaim("uid",uid)
                .withClaim("scope",scope)
                .withExpiresAt(map.get("expiredTime"))
                .withIssuedAt(map.get("now"))
                .sign(algorithm);
    }
    private static Map<String, Date> calculateExpiredIssues() {
        Map<String,Date> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.SECOND,JwtToken.expiredTimeIn);
        map.put("now",now);
        map.put("expiredTime",calendar.getTime());
        return map;
    }

    public static Boolean verifyToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            return false;
        }
        return true;
    }
}
