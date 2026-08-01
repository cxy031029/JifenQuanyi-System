package com.heima.jifenquanyi.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.heima.jifenquanyi.admin.dto.AdminLoginDTO;
import com.heima.jifenquanyi.admin.entity.AdminUser;
import com.heima.jifenquanyi.admin.service.AdminAuthService;
import com.heima.jifenquanyi.common.result.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/auth")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    @PostMapping("/login")
    public R<Map<String, Object>> login(@Valid @RequestBody AdminLoginDTO dto) {
        String token = adminAuthService.login(dto.getUsername(), dto.getPassword());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        return R.ok(result);
    }

    @GetMapping("/info")
    public R<Map<String, Object>> info(HttpServletRequest request) {
        AdminUser admin = adminAuthService.current(request.getHeader("Authorization"));
        Map<String, Object> result = new HashMap<>();
        result.put("id", admin.getId());
        result.put("username", admin.getUsername());
        result.put("nickname", admin.getNickname());
        return R.ok(result);
    }

    @PostMapping("/logout")
    public R<Void> logout(HttpServletRequest request) {
        adminAuthService.logout(request.getHeader("Authorization"));
        return R.ok();
    }
}
