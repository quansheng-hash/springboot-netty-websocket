package com.wqs.qctwe.netty.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:wqs
 * @date:2018/11/9
 * @desciption:前后端转输的的数据对象
 */
@Data
public class DataDTO implements Serializable {

    /**
     * 消息的类型：见枚举
     */
    private Integer action;
    /**
     * 消息的对象
     */
    private WebSocketChatMsg chatMsg;
    /**
     * 扩展字段
     */
    private String extand;

}
