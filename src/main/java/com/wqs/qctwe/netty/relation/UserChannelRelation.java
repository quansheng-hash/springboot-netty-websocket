package com.wqs.qctwe.netty.relation;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:wqs
 * @date:2018/11/9
 * @desciption:用户和通道的对应关系
 */
public class UserChannelRelation {

    private static Map<String,Channel> relation=new HashMap<>();

    public static void put(String userId, Channel channel){
        relation.put(userId, channel);
    }

    public static Channel get(String userId){
        return relation.get(userId);
    }

    public void remove(String key){
        relation.remove(key);
    }
}
