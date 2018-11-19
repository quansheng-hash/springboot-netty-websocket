package com.wqs.qctwe.domain;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String userId;

    private String username;

    private String password;

    private String nickname;

    private String imgUrl;

    private String qrcode;

    private String cid;

    private Date createTime;

    private Date updateTime;


}