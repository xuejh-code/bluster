package com.lin.missyou.vo;

import com.lin.missyou.model.Activity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ActivityPureVO {
    private Long id;
    private String title;
    private Date startTime;
    private Date endTime;
    private Boolean online;
    private String remark;
    private String entranceImg;

    public ActivityPureVO (Activity activity){
        BeanUtils.copyProperties(activity,this);
    }

    public ActivityPureVO(Object object){
        BeanUtils.copyProperties(object,this);
    }
}
