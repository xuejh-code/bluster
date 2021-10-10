package com.lin.missyou.vo;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

//泛型类的继承
//整个PagingDozer构建的思想是和Paging差不多的，但是会承担多一个额外的任务：做属性复制；
//PagingDozer是继承Paging的，所以Paging里面的成员变量它都有，也必须有，因为最后的返回结果，PagingDozer应该和Paging是一样的；
public class PagingDozer<T,K> extends Paging{
    @SuppressWarnings("unchecked")
    public PagingDozer(Page<T> pageT,Class<K> classK){
        this.initPageParameters(pageT);
        //items的赋值，不能像Paging一样直接getContent，还需要做Dozer的复制；
        //如果要做属性复制，原数据是要拿到的；
        List<T> tList = pageT.getContent();
        //然后再做属性的复制，之前在SpuController里是对具体的Spu做处理，这里要对不确定的泛型进行处理
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        //之前做属性复制既需要知道原数据的类型，又需要知道目标数据的类型
        //这边的原数据类型只是一个T，目标数据类型也不确定，处理方案是从外面把类型传进来，再增加一个K泛型参数；
        List<K> voList = new ArrayList<>();
        tList.forEach(t -> {
            //这里目标的元类无法直接用泛型K.class来传入，还是要通过构造函数传入进来；
            K vo = mapper.map(t,classK);
            voList.add(vo);
        });
        this.setItems(voList);
    }
}
