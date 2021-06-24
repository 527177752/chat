package com.example.chat.service.impl;

import com.example.chat.entity.User;
import com.example.chat.mapper.UserMapper;
import com.example.chat.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nicai
 * @since 2021-06-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
