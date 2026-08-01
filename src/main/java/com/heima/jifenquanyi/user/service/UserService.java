package com.heima.jifenquanyi.user.service;

import cn.hutool.core.bean.BeanUtil;
import com.heima.jifenquanyi.common.exception.BizException;
import com.heima.jifenquanyi.common.util.UserContext;
import com.heima.jifenquanyi.user.dto.UserVO;
import com.heima.jifenquanyi.user.entity.User;
import com.heima.jifenquanyi.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public UserVO info() {
        Long userId = UserContext.getUserId();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(user, vo);
        return vo;
    }

    public void update(UserVO vo) {
        Long userId = UserContext.getUserId();
        User user = new User();
        user.setId(userId);
        user.setNickname(vo.getNickname());
        user.setAvatar(vo.getAvatar());
        userMapper.updateById(user);
    }
}
