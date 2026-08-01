package com.heima.jifenquanyi.bill.dto;

import lombok.Data;

@Data
public class FlowPageDTO {

    private Integer bizType;
    private Integer current = 1;
    private Integer size = 10;
}
