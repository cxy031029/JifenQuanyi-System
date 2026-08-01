package com.heima.jifenquanyi.point.dto;

import lombok.Data;

@Data
public class SignInVO {

    private boolean todaySigned;
    private Integer continueDays;
    private Integer point;
}
