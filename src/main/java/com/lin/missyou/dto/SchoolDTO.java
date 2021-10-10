package com.lin.missyou.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class SchoolDTO {
    @Length(min = 2,message = "字符长度必须大于2")
    private String schoolName;
}
