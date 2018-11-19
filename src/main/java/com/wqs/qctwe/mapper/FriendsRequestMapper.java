package com.wqs.qctwe.mapper;

import com.wqs.qctwe.domain.FriendsRequest;
import com.wqs.qctwe.domain.MyFriends;
import com.wqs.qctwe.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendsRequestMapper {
    int deleteByPrimaryKey(String id);

    int insert(FriendsRequest record);

    int insertSelective(FriendsRequest record);

    FriendsRequest selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FriendsRequest record);

    int updateByPrimaryKey(FriendsRequest record);

    FriendsRequest slelectById(FriendsRequest friendsRequest);

    List<User> getRequestList(String acceptUserId);

    int  deleteById(@Param("accpetUserId") String accpetUserId, @Param("sendUserId") String sendUserId);

}