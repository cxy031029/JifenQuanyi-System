package com.heima.jifenquanyi.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.jifenquanyi.common.result.PageResult;
import com.heima.jifenquanyi.common.result.R;
import com.heima.jifenquanyi.user.entity.User;
import com.heima.jifenquanyi.user.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserMapper userMapper;

    public AdminUserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping
    public R<PageResult<User>> page(@RequestParam(defaultValue = "1") long current,
                                    @RequestParam(defaultValue = "10") long size,
                                    @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .and(StrUtil.isNotBlank(keyword), w -> w
                        .like(User::getPhone, keyword)
                        .or()
                        .like(User::getNickname, keyword))
                .orderByDesc(User::getCreateTime);
        Page<User> page = userMapper.selectPage(new Page<>(current, size), wrapper);
        PageResult<User> result = new PageResult<>();
        result.setRecords(page.getRecords());
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        return R.ok(result);
    }

    @PutMapping("/{id}/status")
    public R<Void> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        userMapper.updateById(user);
        return R.ok();
    }
}
