package com.wqs.qctwe.netty.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:wqs
 * @date:2018/11/9
 * @desciption:聊天消息对象
 */
@Data
public class WebSocketChatMsg implements Serializable {

    private String sendUserId;
    private String acceptUserId;
    private String msg;
    private String msgId;
}
