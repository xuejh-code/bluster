package com.lin.missyou.api.v1;

import com.lin.missyou.dto.TokenDTO;
import com.lin.missyou.dto.TokenGetDTO;
import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.service.WxAuthenticationService;
import com.lin.missyou.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "token")
public class TokenController {

    @Autowired
    private WxAuthenticationService wxAuthenticationService;

    @PostMapping("")
    public Map<String,String> getToken(@RequestBody @Validated TokenGetDTO userData){
        //定义一个Map<String,String>作为返回值；
        Map<String,String> map = new HashMap<>();
        //当用户传入了相关的校验参数之后，如果校验通过，需要生成一个JWT令牌，这令牌就是这里定义的token
        String token = null;
        //下一步校验用户传进来的userData的数据，同时进行token的生成
        //条件分支语句对于不同登录方式的不同处理
        switch (userData.getType()){
            case USER_WX:
                token = wxAuthenticationService.code2Session(userData.getAccount());
                break;
            case USER_Email:
                break;
            default:
                throw new NotFoundException(10003);
        }
        map.put("token",token);
        return map;
    }

    @PostMapping("/verify")
    public Map<String,Boolean> verify(@RequestBody TokenDTO token){
        Map<String,Boolean> map = new HashMap<>();
        Boolean valid = JwtToken.verifyToken(token.getToken());
        map.put("is_valid",valid);
        return map;
    }

}
