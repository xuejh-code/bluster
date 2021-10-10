package com.lin.missyou.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.User;
import com.lin.missyou.repository.UserRepository;
import com.lin.missyou.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WxAuthenticationService {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private UserRepository userRepository;

    //读取配置文件中的三个配置项
    @Value("${wx.code2session}")
    private String code2SessionUrl;
    @Value("${wx.appid}")
    private String appid;
    @Value("${wx.secret}")
    private String secret;

    //1、访问微信服务器，验证code是否合法，有没有过期
    //2、如果合法要生成JWT令牌，否则的话，抛出一个异常
    //问题：code怎么从前端发过来？
    public String code2Session(String code){
        //将模版字符串和相应的参数拼接起来；
        String url = MessageFormat.format(this.code2SessionUrl,this.appid,this.secret,code);
        //后端发送http请求；
        RestTemplate rest = new RestTemplate();
        Map<String,Object> session = new HashMap<>();
        //第二个参数是标明返回类型；
        String sessionText = rest.getForObject(url,String.class);
        //在java中一切都以对象的形式来处理的
        //方案1：根据微信的返回结果，定义一个class类，进行反序列化，将字符串变成类；
        //方案2：Map的数据结构，用objectMapper的readValue方法；
        try {
            //反序列化
            //做一个容错处理，判空
            session = mapper.readValue(sessionText,Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this.registerUser(session);
    }

    //写一个复杂的业务的时候每一个细节都考虑周到是不可能的，很多地方都是要做容错处理的
    //如果要把这块的错误处理做得非常详细的话，要对每一个错误码做出具体处理的反应
    //但是错误码很多，还有一些情况是未知的，比如说服务器网络断了，微信的服务器坏了
    //把能够处理的一些特殊情况而不是能想到的，都把它处理一下，一些非常极端的特殊情况可以不去处理；
    //以后对项目的完善和不断重构的过程，给其进行处理；
    //没有具体的异常处理的话，500的错误不太容易调试，但是如果问题调试出来了，用更详细的异常处理的方式把它给补全；
    //发现错误之后，需要给他明确成一个异常，而不是直接改掉；
    private String registerUser(Map<String,Object> session){
        //判断一下openid取出来到底有没有值，如果没有需要提示一下或者写入日志；
        String openid = (String) session.get("openid");
        if (openid == null){
            throw new ParameterException(20004);
        }
        Optional<User> userOptional = this.userRepository.findByOpenid(openid);
        if (userOptional.isPresent()){
            //TODO:返回JWT令牌
            return JwtToken.makeToken(userOptional.get().getId());
        }
        User user = User.builder().openid(openid).build();
        userRepository.save(user);
        //TODO:返回JWT令牌
        Long uid = user.getId();
        return JwtToken.makeToken(uid);
    }

}
