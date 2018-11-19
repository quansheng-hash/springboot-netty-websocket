package com.wqs.qctwe.util;

import org.springframework.util.DigestUtils;

/**
 * @author:wqs
 * @date:2018/11/3
 * @desciption:
 */
public class MD5Utils {

    public static String getMD5(String password){
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
