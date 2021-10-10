package com.lin.missyou.core.enumeration;

import lombok.val;

public enum LoginType {
    USER_WX(0,"微信登录"),
    USER_Email(1,"邮箱登陆");

    private Integer value;

    //加了这个私有的构造函数上面的枚举类型就不再是无参的形式了，必须传入value和description；
    //private的修饰符不加也可以，但是不能用public
    LoginType (Integer value,String description){
        this.value =value;
    }

    //在枚举中定义的成员变量和方法都是为一项一项的枚举服务的
    //定义了一个构造函数，所有的枚举都需要传入构造函数的参数
    //如果定义了一个test方法，那所有枚举都会有一个test方法
}
