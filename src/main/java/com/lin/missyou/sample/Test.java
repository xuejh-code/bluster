package com.lin.missyou.sample;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype",proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Setter
public class Test {
    private String name = "7";
}
