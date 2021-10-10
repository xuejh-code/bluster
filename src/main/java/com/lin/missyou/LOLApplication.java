package com.lin.missyou;

import com.lin.missyou.sample.EnableLOLConfiguration;
import com.lin.missyou.sample.HeroConfiguration;
import com.lin.missyou.sample.ISkill;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan
@Import(HeroConfiguration.class)
@EnableLOLConfiguration
public class LOLApplication{
    public static void main(String[] args){
        ConfigurableApplicationContext context = new SpringApplicationBuilder(LOLApplication.class).web(WebApplicationType.NONE).run(args);
        //目的是为了验证配置类的Bean是否加入到IOC容器里面了
        ISkill iSkill = (ISkill)context.getBean("irelia");
        iSkill.r();
    }
}
