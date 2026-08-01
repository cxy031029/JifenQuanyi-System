package com.heima.jifenquanyi.exchange.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.jifenquanyi.common.result.R;
import com.heima.jifenquanyi.exchange.dto.ExchangeDTO;
import com.heima.jifenquanyi.exchange.entity.ExchangeRecord;
import com.heima.jifenquanyi.exchange.service.ExchangeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @PostMapping
    public R<Void> exchange(@Valid @RequestBody ExchangeDTO dto) {
        exchangeService.exchange(dto);
        return R.ok();
    }

    @GetMapping("/records")
    public R<Page<ExchangeRecord>> records(@RequestParam(defaultValue = "1") int current,
                                           @RequestParam(defaultValue = "10") int size) {
        return R.ok(exchangeService.records(current, size));
    }
}
