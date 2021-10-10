package com.lin.missyou.sample.hero;

import com.lin.missyou.sample.ISkill;
import org.springframework.stereotype.Component;


public class Diana implements ISkill {
    private String skillName = "Diana R";
    private String name;
    private Integer age;

    public Diana(String name,Integer age){
        this.name = name;
        this.age = age;
    }

    public Diana(){
        System.out.println("Hello,7yue");
    }

    public void q(){
        System.out.println("Diana Q");
    }

    public void w(){
        System.out.println("Diana W");
    }

    public void e(){
        System.out.println("Diana E");
    }

    public void r(){
        System.out.println("Diana R");
    }
}
