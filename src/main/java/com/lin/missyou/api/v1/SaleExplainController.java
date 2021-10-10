package com.lin.missyou.api.v1;

import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.model.SaleExplain;
import com.lin.missyou.service.SaleExplainService;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sale_explain")
public class SaleExplainController {
    @Autowired
    private SaleExplainService saleExplainService;

    @GetMapping("/fixed")
    public List<SaleExplain> getFixed(){
        List<SaleExplain> saleExplains = this.saleExplainService.getSaleExplainFixedList();
        if (saleExplains.isEmpty()){
            throw new NotFoundException(30011);
        }
        return saleExplains;
    }
}
