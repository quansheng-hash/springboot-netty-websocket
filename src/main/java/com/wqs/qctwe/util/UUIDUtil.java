package com.wqs.qctwe.util;

import java.util.UUID;

/**
 * @author:wqs
 * @date:2018/11/3
 * @desciption:
 */
public class UUIDUtil {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","" );
    }
}
