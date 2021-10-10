package com.lin.missyou.dto;

import com.lin.missyou.dto.validators.PasswordEqual;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;

//@Setter
//@Getter
@Builder
@Getter
@PasswordEqual(message = "两次密码不一样")
public class PersonDTO {
    @Length(min = 2,max = 10,message = "名字的长度范围为2-10")
    private String name;
    private Integer age;

    @Valid
    private SchoolDTO schoolDTO;

    private String password1;
    private String password2;
}

//数据传输对象
