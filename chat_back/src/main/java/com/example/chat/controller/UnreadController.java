package com.example.chat.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.chat.entity.Unread;
import com.example.chat.service.IUnreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author nicai
 * @since 2021-06-21
 */
@RestController
@RequestMapping("/chat/unread")
public class UnreadController {
    @Autowired
    private IUnreadService unreadService;

    @GetMapping("/getUnread.do")
    public @ResponseBody
    List<Unread> findById(@RequestParam Integer id) {
        Assert.notNull(id, "id不能为空！");
        QueryWrapper<Unread> q = new QueryWrapper<>();
        q.lambda().eq(Unread::getRecId,id);
        List<Unread> list = unreadService.list(q);
        unreadService.remove(q);
        return list;
    }
}
