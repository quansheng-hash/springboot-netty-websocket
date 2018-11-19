package com.wqs.qctwe.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMsg {
    private String id;

    private String sendUserId;

    private String acceptUserId;

    private String msg;

    private Integer signFlag;

    private Date createTime;


}