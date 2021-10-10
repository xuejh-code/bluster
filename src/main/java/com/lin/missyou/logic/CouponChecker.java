package com.lin.missyou.logic;

import com.lin.missyou.bo.SkuOrderBO;
import com.lin.missyou.core.enumeration.CouponType;
import com.lin.missyou.core.money.IMoneyDiscount;
import com.lin.missyou.exception.http.ForbiddenException;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.Category;
import com.lin.missyou.model.Coupon;
import com.lin.missyou.util.CommonUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CouponChecker {

    private Coupon coupon;
    private IMoneyDiscount iMoneyDiscount;

    public CouponChecker(Coupon coupon, IMoneyDiscount iMoneyDiscount){
        this.coupon = coupon;
        this.iMoneyDiscount = iMoneyDiscount;
    }

    public void isOk(){
        Date now = new Date();
        Boolean isInTimeLine = CommonUtil.isInTimeLine(now,this.coupon.getStartTime(),this.coupon.getEndTime());
        if (!isInTimeLine){
            throw new ForbiddenException(40005);
        }
    }

    public void finalTotalPriceIsOk(BigDecimal orderFinalTotalPrice,BigDecimal serverTotalPrice){
        BigDecimal serverFinalTotalPrice;

        switch (CouponType.toType(this.coupon.getType())){
            case FULL_OFF:
                serverFinalTotalPrice = this.iMoneyDiscount.discount(serverTotalPrice,this.coupon.getRate());
                break;
            case FULL_MINUS:
            case NO_THERSHOLD_MINUS:
                serverFinalTotalPrice = serverTotalPrice.subtract(this.coupon.getMinus());
                if (serverFinalTotalPrice.compareTo(new BigDecimal("0")) <= 0){
                    throw new ForbiddenException(50008);
                }
                break;
            default:
                throw new ParameterException(40009);
        }
        int compare = serverFinalTotalPrice.compareTo(orderFinalTotalPrice);
        if (compare != 0){
            throw new ForbiddenException(50008);
        }

    }

    public void canBeUsed(List<SkuOrderBO> skuOrderBOList,BigDecimal serverTotalPrice){
        BigDecimal orderCategoryPrice;
        if (this.coupon.getWholeStore()){
            orderCategoryPrice = serverTotalPrice;
        }
        else{
            List<Long> cidList = this.coupon.getCategoryList()
                    .stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
            orderCategoryPrice = this.getSumByCategoryList(skuOrderBOList,cidList);
        }
        this.couponCanBeUsed(orderCategoryPrice);
    }

    private void couponCanBeUsed(BigDecimal orderCategoryPrice){
        switch (CouponType.toType(this.coupon.getType())){
            case FULL_OFF:
            case FULL_MINUS:
                int compare = this.coupon.getFullMoney().compareTo(orderCategoryPrice);
                if (compare > 0){
                    throw new ParameterException(40008);
                }
                break;
            case NO_THERSHOLD_MINUS:
                break;
            default:
                throw new ParameterException(40009);
        }
    }

    private BigDecimal getSumByCategoryList(List<SkuOrderBO> skuOrderBOList,List<Long> cidList){
        BigDecimal sum = cidList.stream()
                .map(cid->this.getSumByCategory(skuOrderBOList,cid))
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal("0"));
        return sum;
    }

    private BigDecimal getSumByCategory(List<SkuOrderBO> skuOrderBOList,Long cid){
        BigDecimal sum = skuOrderBOList.stream()
                .filter(sku->sku.getCategoryId().equals(cid))
                .map(SkuOrderBO::getTotal)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal("0"));
        return sum;
    }

}
