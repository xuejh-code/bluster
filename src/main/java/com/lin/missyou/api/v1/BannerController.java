package com.lin.missyou.api.v1;

import com.lin.missyou.core.interceptors.ScopeLevel;
import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.model.Banner;
import com.lin.missyou.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/banner")
@Validated
public class BannerController {
//    @Autowired
//    private ISkill iSkill;
//
//    @Autowired
//    private IConnect iConnect;

    @Autowired
    private BannerService bannerService;

    @GetMapping("/name/{name}")
//    @ScopeLevel
    public Banner getByName(@PathVariable @NotBlank String name){
        Banner banner = bannerService.getByName(name);
        if (banner == null){
            throw new NotFoundException(30005);
        }
        return banner;
    }

//    @PostMapping("/test/{id}")
//    public PersonDTO test(@PathVariable @Range(min = 1,max = 10,message = "不可以超过10噢😯") Integer id,
//                       @RequestParam @Length(min = 5,message = "名字需要在五个字符以上") String name,
//                       @RequestBody @Validated PersonDTO person) {
//        iSkill.r();
//        PersonDTO dto = new PersonDTO();
//        dto.setName("9yue9");
//        dto.setAge(25);

//        PersonDTO dto = PersonDTO.builder()
//                .name("7yue")
//                .age(18)
//                .build();
//        return dto;
//        throw new ForbiddenException(10000);
//        return "Hello,九月";
//    }

//    @GetMapping("/test1")
//    public void test1(){
//        iConnect.connect();
//    }
}
