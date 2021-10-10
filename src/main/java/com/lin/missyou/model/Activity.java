package com.lin.missyou.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Where(clause = "delete_time is null and online = 1")
public class Activity extends BaseEntity{
    @Id
    private Long id;
    private String title;
    private String name;
    private String description;
//    private Long activityCoverId;
    private Date startTime;
    private Date endTime;
    private Boolean online;
    private String remark;
    private String entranceImg;
    private String internalTopImg;

//    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "")
//    @JoinTable(name = "activity_category",
//        joinColumns = @JoinColumn(name = "activity_id"),
//        inverseJoinColumns = @JoinColumn(name = "category_id"))
//    private List<Category> categoryList;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "activityId")
    private List<Coupon> couponList;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "activity_coupon",
//        joinColumns = @JoinColumn(name = "activity_id"),
//        inverseJoinColumns = @JoinColumn(name = "coupon_id"))
//    private List<Coupon> couponList;
}
