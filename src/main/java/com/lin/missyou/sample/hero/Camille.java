package com.lin.missyou.sample.hero;

import com.lin.missyou.sample.ISkill;
import org.springframework.stereotype.Component;


public class Camille implements ISkill {

    private String skillName = "Camille R";
    private String name;
    private Integer age;

    public Camille(String name,Integer age){
        this.name = name;
        this.age = age;
    }

    public Camille(){
        System.out.println("Hello,7yue");
    }

    public void q(){
        System.out.println("Camille Q");
    }

    public void w(){
        System.out.println("Camille W");
    }

    public void e(){
        System.out.println("Camille E");
    }

    public void r(){
        System.out.println(this.skillName);
    }
}
