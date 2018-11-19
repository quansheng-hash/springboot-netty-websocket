package com.wqs.qctwe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wqs.qctwe.domain.ChatMsg;
import com.wqs.qctwe.domain.User;
import com.wqs.qctwe.dto.SearchFriendsDTO;

import java.util.List;

/**
 * @author:wqs
 * @date:2018/11/3
 * @desciption:
 */
public interface UserService {

    int register(User user);

    User login(User user);

    User updateNickname(User user);

    SearchFriendsDTO searchFriendsByUsername(String userId,String username);

    int addFriendRequest(String sendUserId,String acceptUserId);

    List<User> getRequestList(String userId);

    int friendRequestResult(String acceptUserId,String sendUserId,Integer type);

    List<User> getFriendsListByUserId(String userId);

    void saveChatMsg(ChatMsg msg1);

    void updateChatStatus(List<String> list);

    List<ChatMsg> getUnReadMsg(String userId);
}
