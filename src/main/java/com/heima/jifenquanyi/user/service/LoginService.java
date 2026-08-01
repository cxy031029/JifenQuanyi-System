package com.heima.jifenquanyi.user.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.jifenquanyi.common.exception.BizException;
import com.heima.jifenquanyi.point.entity.PointAccount;
import com.heima.jifenquanyi.point.mapper.PointAccountMapper;
import com.heima.jifenquanyi.user.dto.LoginDTO;
import com.heima.jifenquanyi.user.dto.RegisterDTO;
import com.heima.jifenquanyi.user.entity.User;
import com.heima.jifenquanyi.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    private final UserMapper userMapper;
    private final SessionService sessionService;
    private final PointAccountMapper pointAccountMapper;

    public LoginService(UserMapper userMapper, SessionService sessionService, PointAccountMapper pointAccountMapper) {
        this.userMapper = userMapper;
        this.sessionService = sessionService;
        this.pointAccountMapper = pointAccountMapper;
    }

    public String login(LoginDTO dto) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, dto.getPhone()));
        if (user == null) {
            throw new BizException("账号不存在");
        }
        if (!DigestUtil.md5Hex(dto.getPassword()).equals(user.getPassword())) {
            throw new BizException("密码错误");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BizException("账号已禁用");
        }
        return sessionService.createSession(user.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO dto) {
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getPhone, dto.getPhone()));
        if (count > 0) {
            throw new BizException("手机号已注册");
        }
        User user = new User();
        user.setPhone(dto.getPhone());
        user.setPassword(DigestUtil.md5Hex(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setStatus(1);
        userMapper.insert(user);

        PointAccount account = new PointAccount();
        account.setUserId(user.getId());
        account.setTotalPoint(0);
        account.setFrozenPoint(0);
        account.setExpiredPoint(0);
        account.setVersion(0);
        pointAccountMapper.insert(account);
    }
}
