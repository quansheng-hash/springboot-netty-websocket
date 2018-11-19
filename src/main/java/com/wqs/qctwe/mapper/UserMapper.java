package com.wqs.qctwe.mapper;

import com.wqs.qctwe.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(String userId);

    int insertSelective(User record);



    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int insert(User user);
    User login(User user);
    User isUserExist(String username);
    User selectById(String userId);
    int updateNickname(User user);
}