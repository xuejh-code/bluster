package com.lin.missyou.api.v1;

import com.lin.missyou.bo.PageCounter;
import com.lin.missyou.model.Spu;
import com.lin.missyou.service.SearchService;
import com.lin.missyou.util.CommonUtil;
import com.lin.missyou.vo.PagingDozer;
import com.lin.missyou.vo.SpuSimplifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("")
    public PagingDozer<Spu, SpuSimplifyVO> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "10") Integer count
    ) {
        PageCounter counter = CommonUtil.convertToPageParameter(start,count);
        Page<Spu> page = this.searchService.search(q,counter.getPage(),counter.getCount());
        return new PagingDozer<>(page,SpuSimplifyVO.class);
    }
}
