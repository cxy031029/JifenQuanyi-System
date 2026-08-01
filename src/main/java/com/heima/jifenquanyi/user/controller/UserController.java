package com.heima.jifenquanyi.user.controller;

import com.heima.jifenquanyi.common.result.R;
import com.heima.jifenquanyi.user.dto.UserVO;
import com.heima.jifenquanyi.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public R<UserVO> info() {
        return R.ok(userService.info());
    }

    @PutMapping("/info")
    public R<Void> update(@RequestBody UserVO vo) {
        userService.update(vo);
        return R.ok();
    }
}
