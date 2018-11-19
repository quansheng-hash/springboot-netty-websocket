package com.wqs.qctwe.dto;

import com.wqs.qctwe.domain.User;
import lombok.Data;

/**
 * @author:wqs
 * @date:2018/11/5
 * @desciption:
 */
@Data
public class SearchFriendsDTO {

    private String msg;
    private User  user;

    public SearchFriendsDTO(String msg, User user) {
        this.msg = msg;
        this.user = user;
    }
}
