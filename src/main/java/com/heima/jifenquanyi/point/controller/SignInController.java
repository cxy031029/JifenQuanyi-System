package com.heima.jifenquanyi.point.controller;

import com.heima.jifenquanyi.common.result.R;
import com.heima.jifenquanyi.point.dto.SignInVO;
import com.heima.jifenquanyi.point.service.SignInService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signin")
public class SignInController {

    private final SignInService signInService;

    public SignInController(SignInService signInService) {
        this.signInService = signInService;
    }

    @PostMapping
    public R<SignInVO> signIn() {
        return R.ok(signInService.signIn());
    }

    @GetMapping("/today")
    public R<SignInVO> today() {
        return R.ok(signInService.today());
    }
}
