package com.example.chat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author nicai
 * @since 2021-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("chat_unread")
public class Unread implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发送用户id
     */
    private Integer sendId;

    /**
     * 发送用户账号
     */
    private String sendAcct;

    /**
     * 发送用户昵称
     */
    private String sendName;

    /**
     * 接收用户id
     */
    private Integer recId;

    /**
     * 接收用户账号
     */
    private String recAcct;

    /**
     * 接收用户昵称
     */
    private String recName;

    /**
     * 内容
     */
    private String content;

    /**
     * 发送用户头像
     */
    private String image;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
