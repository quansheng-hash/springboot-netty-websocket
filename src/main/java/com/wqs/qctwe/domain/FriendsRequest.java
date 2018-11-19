package com.wqs.qctwe.domain;

import lombok.Data;

import java.util.Date;

@Data
public class FriendsRequest {
    private String id;

    private String sendUserId;

    private String accpetUserId;

    private Date requestDateTime;


}