package com.wqs.qctwe.enums;

public enum WebSocketActionEunm {
    CONNECT(1,"建立连接"),
    CHAT(2,"聊天信息"),
    SINGNED(3,"签收消息"),
    KEEPALIVE(4,"保持心跳连接"),
    PULL_FRIEND(5,"拉取好友列表");

    private Integer action;
    private String msg;

    WebSocketActionEunm(Integer action, String msg) {
        this.action = action;
        this.msg = msg;
    }

    public Integer getAction() {
        return action;
    }

    public String getMsg() {
        return msg;
    }
}
