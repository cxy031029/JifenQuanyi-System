package com.heima.jifenquanyi.point.dto;

import lombok.Data;

@Data
public class TaskDTO {

    private Long id;
    private String taskCode;
    private String taskName;
    private Integer point;
    private Integer dailyLimit;
    private Integer status;
}
