package com.heima.jifenquanyi.exchange.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExchangeDTO {

    @NotNull(message = "商品id不能为空")
    private Long productId;

    @Min(value = 1, message = "兑换数量不能小于1")
    private Integer quantity = 1;
}
