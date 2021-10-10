package com.lin.missyou.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

//Paging是一个公共对象，并不一定特指spu,仅仅为了spu就封装一个类是不划算的，所以需要用到泛型
//java作为一个强类型的语言，不能灵活切换类型，所以需要使用泛型
@Getter
@Setter
@NoArgsConstructor
public class Paging<T> {
    private Long total;//总数量，前端有时候会需要知道数据库里有多少条记录；
    private Integer count;//当前请求的数据总共应该有多少条
    private Integer page;//页码
    private Integer totalPage;//总共有多少页
    private List<T> items;//查询出来的page里的结果，如果要特指spu的话，List<Spu>

    //Paging是要服务于各种各样的对象的，所以这里传入的Page也要使用泛型T
    //所有从数据库里查询出来的数据都是在这个pageT里面
    //类比SpuController里的Page<Spu>
    public Paging(Page<T> pageT){
        this.initPageParameters(pageT);
        this.items = pageT.getContent();
        //getContent方法可以拿到真实的数据的数据；
    }

    //写一个方法进行参数的赋值，将pageT里的内容赋值给成员变量；
    void initPageParameters(Page<T> pageT){
        //操作pageT
        this.total = pageT.getTotalElements();
        this.count = pageT.getSize();
        this.page = pageT.getNumber();
        this.totalPage = pageT.getTotalPages();
        //这些参数其实可以不用返回到前端去，有些参数前端是可以计算出来的，但是服务端可以通过JPA的page对象很方便的拿到这些数据就没必要让前端计算了；
    }
}
