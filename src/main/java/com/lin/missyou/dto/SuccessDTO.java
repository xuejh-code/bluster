package com.lin.missyou.dto;

import com.lin.missyou.util.HttpRequestProxy;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessDTO {
    private Integer code = 0;
    private String message = "ok";
    private String request = HttpRequestProxy.getRequestUrl();
}
