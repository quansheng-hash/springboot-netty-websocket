package com.wqs.qctwe.mapper;

import com.wqs.qctwe.domain.MyFriends;
import com.wqs.qctwe.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyFriendsMapper {
    int deleteByPrimaryKey(String id);

    int insert(MyFriends record);

    int insertSelective(MyFriends record);

    MyFriends selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MyFriends record);

    int updateByPrimaryKey(MyFriends record);

    MyFriends getById(@Param("myUserId") String myUserId,@Param("myFriendsUserId") String myFriendsUserId);

    List<User> getFriendsListByUserId(String userId);
}