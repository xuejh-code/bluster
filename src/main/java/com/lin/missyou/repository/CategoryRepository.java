package com.lin.missyou.repository;

import com.lin.missyou.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    //这里其实可以不用写任何方法，因为可以直接调用findAll方法，但是需要考虑前端使用接口数据的时候是不是符合逻辑
    //如果按照数据库的结构返回会去，势必要前端了解数据库的结构，这也是VO存在的意义，如果所有的结构都按数据库的来，VO就没有意义了；
    //理想情况下的数据结构：{List,List,List}
    //折中的方式，既要改结构方便，也不能让前端使用太麻烦；返回两个List，一个是一级分类，另一个是二级分类；
    //数据结构到底怎么返回需要去和前端有个协商；

    //怎么查询数据库？用JPA默认的生成的findAll？但是要上面的数据结构形式{List,List,List}需要编写额外的代码在java中进行处理；
    //如果选择查询两次数据库就不需要在java代码里进行处理了
    //第一种方式查询一次，自己处理，不难但是比较繁琐，还需要考虑排序的问题；Category的model里还有index字段；

    List<Category> findAllByIsRootOrderByIndexAsc(Boolean isRoot);
}
