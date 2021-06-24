package com.example.chat;

import com.example.chat.service.IUserService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.chat.mapper")
public class ChatApplication {
    @Autowired
    private IUserService userService;

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

}
