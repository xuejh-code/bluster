package com.lin.missyou.api.v1;

import com.lin.missyou.dto.SuccessDTO;
import com.lin.missyou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/wx_info")
    public SuccessDTO updateUserInfo(@RequestBody Map<String,Object> user){
        userService.updateUserWxInfo(user);
        return new SuccessDTO();
    }
}
