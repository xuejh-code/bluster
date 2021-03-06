package com.lin.missyou.service;

import com.lin.missyou.core.LocalUser;
import com.lin.missyou.core.enumeration.OrderStatus;
import com.lin.missyou.core.money.IMoneyDiscount;
import com.lin.missyou.dto.OrderDTO;
import com.lin.missyou.dto.SkuInfoDTO;
import com.lin.missyou.exception.http.ForbiddenException;
import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.logic.CouponChecker;
import com.lin.missyou.logic.OrderChecker;
import com.lin.missyou.model.*;
import com.lin.missyou.repository.CouponRepository;
import com.lin.missyou.repository.OrderRepository;
import com.lin.missyou.repository.SkuRepository;
import com.lin.missyou.repository.UserCouponRepository;
import com.lin.missyou.util.CommonUtil;
import com.lin.missyou.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Scope()
public class OrderService {
    @Autowired
    private SkuService skuService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private IMoneyDiscount iMoneyDiscount;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${missyou.order.max-sku-limit}")
    private int maxSkuLimit;

    @Value("${missyou.order.pay-time-limit}")
    private Integer payTimeLimit;

    @Transactional
    public Long placeOrder(Long uid,OrderDTO orderDTO,OrderChecker orderChecker){
        String orderNo = OrderUtil.makeOrderNo();
        Calendar now = Calendar.getInstance();
        Calendar now1 = (Calendar) now.clone();
        Date expiredTime = CommonUtil.addSomeSeconds(now,this.payTimeLimit).getTime();
        Order order = Order.builder()
                .orderNo(orderNo)
                .totalPrice(orderDTO.getTotalPrice())
                .finalTotalPrice(orderDTO.getFinalTotalPrice())
                .userId(uid)
                .totalCount(orderChecker.getTotalCount().longValue())
                .snapImg(orderChecker.getLeaderImg())
                .snapTitle(orderChecker.getLeaderTitle())
                .status(OrderStatus.UNPAID.value())
                .expiredTime(expiredTime)
                .placedTime(now1.getTime())
                .build();
        order.setSnapAddress(orderDTO.getAddress());
        order.setSnapItems(orderChecker.getOrderSkuList());
        this.orderRepository.save(order);
        this.reduceStock(orderChecker);
        Long couponId = -1L;
        if (orderDTO.getCouponId() != null){
            this.writeOffCoupon(orderDTO.getCouponId(), order.getId(), uid);
            couponId = orderDTO.getCouponId();
        }
        //???????????????????????????
        this.sendToRedis(order.getId(), uid, couponId);
        return order.getId();
    }

    private void sendToRedis(Long oid,Long uid,Long couponId){
        String key = uid.toString()+","+oid.toString()+","+couponId.toString();
        try {
            stringRedisTemplate.opsForValue().set(key,"1",this.payTimeLimit, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public Page<Order> getUnpaid(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page,size, Sort.by("createTime").descending());
        Long uid = LocalUser.getUser().getId();
        Date now = new Date();
        return this.orderRepository.findByExpiredTimeGreaterThanAndStatusAndUserId(now,OrderStatus.UNPAID.value(), uid,pageable);
    }

    public Page<Order> getByStatus(Integer status,Integer page,Integer size){
        Pageable pageable = PageRequest.of(page,size,Sort.by("createTime").descending());
        Long uid = LocalUser.getUser().getId();
        if (status == OrderStatus.ALL.value()){
            return this.orderRepository.findByUserId(uid,pageable);
        }
        return this.orderRepository.findByUserIdAndStatus(uid, status, pageable);
    }

    public Optional<Order> getOrder(Long oid){
        Long uid = LocalUser.getUser().getId();
        return this.orderRepository.findFirstByUserIdAndId(uid,oid);
    }

    public void updateOrderPrepayId(Long orderId,String perpayId){
        Optional<Order> order = this.orderRepository.findById(orderId);
        order.ifPresentOrElse(o->{
            o.setPrepayId(perpayId);
            this.orderRepository.save(o);
        },()->{
            throw new NotFoundException(50009);
        });
    }

    private void writeOffCoupon(Long couponId,Long oid,Long uid){
        int result = this.userCouponRepository.writeOff(couponId,oid,uid);
        if (result != 1){
            throw new ForbiddenException(40012);
        }
    }

    private void reduceStock(OrderChecker orderChecker){
        List<OrderSku> orderSkuList = orderChecker.getOrderSkuList();
        for (OrderSku orderSku : orderSkuList){
            int result = this.skuRepository.reduceStock(orderSku.getId(),orderSku.getCount().longValue());
            if (result != 1){
                throw new ParameterException(50003);
            }
        }
    }

    public OrderChecker isOk(Long uid, OrderDTO orderDTO){
        if (orderDTO.getFinalTotalPrice().compareTo(new BigDecimal("0")) <= 0){
            throw new ParameterException(50011);
        }

        List<Long> skuIdList = orderDTO.getSkuInfoList()
                .stream()
                .map(SkuInfoDTO::getId)
                .collect(Collectors.toList());

        List<Sku> skuList = skuService.getSkuListByIds(skuIdList);

        Long couponId = orderDTO.getCouponId();
        CouponChecker couponChecker = null;
        if (couponId!=null){
            Coupon coupon = this.couponRepository.findById(couponId)
                    .orElseThrow(()->new NotFoundException(40004));
            UserCoupon userCoupon = this.userCouponRepository.findFirstByUserIdAndCouponIdAndOrderIdAndStatus(uid,couponId,null,1)
                    .orElseThrow(()->new NotFoundException(50006));
            couponChecker = new CouponChecker(coupon,iMoneyDiscount);
        }
        OrderChecker orderChecker = new OrderChecker(orderDTO,skuList,couponChecker,this.maxSkuLimit);
        orderChecker.isOk();
        return orderChecker;
    }
}
