package com.example.chat.service.impl;

import com.example.chat.entity.Unread;
import com.example.chat.mapper.UnreadMapper;
import com.example.chat.service.IUnreadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nicai
 * @since 2021-06-21
 */
@Service
public class UnreadServiceImpl extends ServiceImpl<UnreadMapper, Unread> implements IUnreadService {

}
