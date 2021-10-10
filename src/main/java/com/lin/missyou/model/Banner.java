package com.lin.missyou.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.hibernate.validator.internal.util.stereotypes.Lazy;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@Where(clause = "delete_time is null")
public class Banner extends BaseEntity{
    @Id
    private Long id;
    private String name;
    private String description;
    private String title;
    private String img;

    @OneToMany
    @JoinColumn(name = "bannerId")
    private List<BannerItem> items;
}
