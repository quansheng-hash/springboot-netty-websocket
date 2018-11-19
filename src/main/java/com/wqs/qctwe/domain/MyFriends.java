package com.wqs.qctwe.domain;

import lombok.Data;

import java.util.Date;

@Data
public class MyFriends {
    private String id;

    private String myUserId;

    private String myFriendUserId;

    private Date createTime;


}