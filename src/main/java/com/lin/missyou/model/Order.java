package com.lin.missyou.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lin.missyou.core.enumeration.OrderStatus;
import com.lin.missyou.dto.OrderAddressDTO;
import com.lin.missyou.util.CommonUtil;
import com.lin.missyou.util.GenericAndJson;
import lombok.*;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "delete_time is null")
@Table(name = "`Order`")
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal totalPrice;
    private Long totalCount;

    private Date expiredTime;
    private Date placedTime;

    private String snapImg;
    private String snapTitle;
    private String snapItems;
    private String snapAddress;

    private String prepayId;
    private BigDecimal finalTotalPrice;
    private Integer status;

    @JsonIgnore
    public OrderStatus getStatusEnum(){
        return OrderStatus.toType(this.status);
    }

    public Boolean needCancel(){
        if (!this.getStatusEnum().equals(OrderStatus.UNPAID)){
            return true;
        }
        Boolean isOutOfDate = CommonUtil.isOutOfDate(this.getExpiredTime());
        if (isOutOfDate){
            return true;
        }
        return false;
    }

    public void setSnapItems(List<OrderSku> orderSkuList){
        if (orderSkuList.isEmpty()){
            return;
        }
        this.snapItems = GenericAndJson.objectToJson(orderSkuList);
    }

    public List<OrderSku> getSnapItems(){
        if (this.snapItems == null){
            return null;
        }
        List<OrderSku> list = GenericAndJson.jsonToList(this.snapItems, new TypeReference<List<OrderSku>>() {
        });
        return list;
    }

    public OrderAddressDTO getSnapAddress(){
        if (this.snapAddress == null){
            return null;
        }
        OrderAddressDTO o = GenericAndJson.jsonToObject(this.snapAddress, new TypeReference<OrderAddressDTO>() {
        });
        return o;
    }

    public void setSnapAddress(OrderAddressDTO address){
        this.snapAddress = GenericAndJson.objectToJson(address);
    }
}
