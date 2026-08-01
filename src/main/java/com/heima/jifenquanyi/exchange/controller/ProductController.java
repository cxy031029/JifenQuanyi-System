package com.heima.jifenquanyi.exchange.controller;

import com.heima.jifenquanyi.common.result.PageResult;
import com.heima.jifenquanyi.common.result.R;
import com.heima.jifenquanyi.exchange.entity.Product;
import com.heima.jifenquanyi.exchange.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public R<PageResult<Product>> list(@RequestParam(defaultValue = "1") int current,
                                       @RequestParam(defaultValue = "10") int size) {
        return R.ok(productService.list(current, size));
    }

    @GetMapping("/{id}")
    public R<Product> detail(@PathVariable Long id) {
        return R.ok(productService.detail(id));
    }
}
