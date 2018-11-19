package com.wqs.qctwe.enums;



public enum FriendStatusEnum {

    SUCCESS(0,"OK"),
    USER_NOTEXIST(1,"用户不存在"),
    USER_ALERTISYOUFRENDS(2,"该用户已经是你的好友了"),
    CANNOTADDYOURSELE(3,"不能添加加自己为好友")
    ;


    private final Integer code;
    private final String msg;

    FriendStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsg(Integer code) {
        for(FriendStatusEnum type:FriendStatusEnum.values()){
            if (type.code==code){
                return type.msg;
            }
        }
        return null;
    }
}
