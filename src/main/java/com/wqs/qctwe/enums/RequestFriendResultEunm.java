package com.wqs.qctwe.enums;

public enum RequestFriendResultEunm {
    PASS(0,"通过好友请求"),
    IGNORE(1,"忽略好友请求");

    private Integer type;
    private String msg;

    RequestFriendResultEunm(Integer type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public Integer getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

}
