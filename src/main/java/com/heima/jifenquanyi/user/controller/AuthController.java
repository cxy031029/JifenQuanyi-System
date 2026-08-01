package com.heima.jifenquanyi.user.controller;

import com.heima.jifenquanyi.common.result.R;
import com.heima.jifenquanyi.user.dto.LoginDTO;
import com.heima.jifenquanyi.user.dto.RegisterDTO;
import com.heima.jifenquanyi.user.service.LoginService;
import com.heima.jifenquanyi.user.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginService loginService;
    private final SessionService sessionService;

    public AuthController(LoginService loginService, SessionService sessionService) {
        this.loginService = loginService;
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public R<String> login(@Valid @RequestBody LoginDTO dto) {
        return R.ok(loginService.login(dto));
    }

    @PostMapping("/register")
    public R<Void> register(@Valid @RequestBody RegisterDTO dto) {
        loginService.register(dto);
        return R.ok();
    }

    @PostMapping("/logout")
    public R<Void> logout(HttpServletRequest request) {
        sessionService.destroy(request.getHeader("Authorization"));
        return R.ok();
    }
}
