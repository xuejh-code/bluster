package com.lin.missyou.model;

import com.lin.missyou.util.MapAndJson;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Where(clause = "delete_time is null")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String openid;
    private String nickname;
    private String email;
    private String mobile;
    private String password;
    private Long unifyUid;

    //建议：如果想做VIP系统的话，可以增加一个分组或身份的字段；
    //分组级别有对应的数字这样就可以和ScopeLevel对应，比如说青铜为4，钻石为32；
//    private String group;

    //方便查询用户使用过的优惠券的导航关系；
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "UserCoupon",
//            joinColumns = @JoinColumn(name = "userId"),
//            inverseJoinColumns = @JoinColumn(name = "couponId"))
//    private List<Coupon> couponList;

    //上面是用户的基础资料，但是用户在微信那边也有它的微信资料；JSON字段；
    //把它拆成一张表会比较好，和user表一对一的关系；
    @Convert(converter = MapAndJson.class)
    private Map<String ,Object> wxProfile;

    //查询用户曾经下过哪些订单的导航关系
//    @OneToMany
//    @JoinColumn(name = "userId")
//    private List<Order> orders = new ArrayList<>();
}
