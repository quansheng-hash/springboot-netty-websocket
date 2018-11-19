package com.wqs.qctwe.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wqs.qctwe.domain.ChatMsg;
import com.wqs.qctwe.domain.FriendsRequest;
import com.wqs.qctwe.domain.MyFriends;
import com.wqs.qctwe.domain.User;
import com.wqs.qctwe.dto.SearchFriendsDTO;
import com.wqs.qctwe.enums.FriendStatusEnum;
import com.wqs.qctwe.enums.RequestFriendResultEunm;
import com.wqs.qctwe.enums.WebSocketActionEunm;
import com.wqs.qctwe.mapper.ChatMsgMapper;
import com.wqs.qctwe.mapper.FriendsRequestMapper;
import com.wqs.qctwe.mapper.MyFriendsMapper;
import com.wqs.qctwe.mapper.UserMapper;
import com.wqs.qctwe.netty.dto.DataDTO;
import com.wqs.qctwe.netty.relation.UserChannelRelation;
import com.wqs.qctwe.service.UserService;
import com.wqs.qctwe.util.MD5Utils;
import com.wqs.qctwe.util.QRCodeUtils;
import com.wqs.qctwe.util.UUIDUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author:wqs
 * @date:2018/11/3
 * @desciption:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MyFriendsMapper myFriendsMapper;
    @Autowired
    private FriendsRequestMapper friendsRequestMapper;
    @Autowired
    private ChatMsgMapper chatMsgMapper;
    @Value("${qrCodePath}")
    private String qrCodePath;

    @Override
    public int register(User user) {

        User userExist = userMapper.isUserExist(user.getUsername());
        if (userExist!=null){
            return 2;
        }
        //注册
        user.setUserId(UUIDUtil.getUUID());
        user.setPassword(MD5Utils.getMD5(user.getPassword()));
        String path=qrCodePath+user.getUsername()+".png";
        QRCodeUtils.createQRCode(path,"userName:"+user.getUsername());
        user.setQrcode(path);
        user.setNickname(user.getUsername());
        return userMapper.insert(user);
    }

    @Override
    public User login(User user) {
        user.setPassword(MD5Utils.getMD5(user.getPassword()));
        return userMapper.login(user);
    }

    @Override
    public User updateNickname(User user) {
        int i = userMapper.updateNickname(user);
        if (i==1){
            return userMapper.selectById(user.getUserId());
        }
        return null;
    }

    @Override
    public SearchFriendsDTO searchFriendsByUsername(String userId,String username) {

        //1.查询用户是否存在
        User user = userMapper.isUserExist(username);
        if(user==null){
            return new SearchFriendsDTO(FriendStatusEnum.USER_NOTEXIST.getMsg(),null);
        }
        //2.查询是否是自己的id
        if(userId.equals(user.getUserId())){
            return new SearchFriendsDTO(FriendStatusEnum.CANNOTADDYOURSELE.getMsg(),user);
        }
        //3.查询是否是自己的好友
        MyFriends myFriends = myFriendsMapper.getById(userId, user.getUserId());
        if(myFriends!=null){
            return new SearchFriendsDTO(FriendStatusEnum.USER_ALERTISYOUFRENDS.getMsg(),user);
        }

        return new SearchFriendsDTO("",user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addFriendRequest(String sendUserId, String acceptUserId) {
        FriendsRequest friendsRequest=new FriendsRequest();
        friendsRequest.setId(UUIDUtil.getUUID());
        friendsRequest.setAccpetUserId(acceptUserId);
        friendsRequest.setSendUserId(sendUserId);
        friendsRequest.setRequestDateTime(new Date());
        FriendsRequest slelect = friendsRequestMapper.slelectById(friendsRequest);
        if (slelect!=null){
            slelect.setRequestDateTime(new Date());
          return friendsRequestMapper.updateByPrimaryKey(slelect);
        }
        return  friendsRequestMapper.insert(friendsRequest);
    }

    @Override
    public List<User> getRequestList(String userId) {
        return friendsRequestMapper.getRequestList(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int friendRequestResult(String acceptUserId, String sendUserId, Integer type)  {
        //忽略
        if (type== RequestFriendResultEunm.IGNORE.getType()){
            return friendsRequestMapper.deleteById(acceptUserId, sendUserId);
        }
        //通过好友时接收方会更新好友列表 但是发送方不会及时更新，所以需要推送信息给发送方拉取好友列表
        if(type==RequestFriendResultEunm.PASS.getType()){
            MyFriends myFriends=new MyFriends();
            MyFriends friends=new MyFriends();
            myFriends.setId(UUIDUtil.getUUID());
            myFriends.setMyUserId(acceptUserId);
            myFriends.setMyFriendUserId(sendUserId);
            myFriends.setCreateTime(new Date());
            friends.setId(UUIDUtil.getUUID());
            friends.setMyUserId(sendUserId);
            friends.setMyFriendUserId(acceptUserId);
            friends.setCreateTime(new Date());
            int insert1 = myFriendsMapper.insert(myFriends);
            int insert2 = myFriendsMapper.insert(friends);
            Channel channel = UserChannelRelation.get(sendUserId);
            if (channel!=null){
                ObjectMapper objectMapper=new ObjectMapper();
                DataDTO dataDTO=new DataDTO();
                dataDTO.setAction(WebSocketActionEunm.PULL_FRIEND.getAction());
                try {
                    channel.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(dataDTO)));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            if (insert1==1&&insert2==1){
                return friendsRequestMapper.deleteById(acceptUserId, sendUserId);
            }
        }
        return 0;
    }

    @Override
    public List<User> getFriendsListByUserId(String userId) {
        return myFriendsMapper.getFriendsListByUserId(userId);
    }

    @Override
    public void saveChatMsg(ChatMsg msg1) {
        chatMsgMapper.insert(msg1);
    }

    @Override
    public void updateChatStatus(List<String> list) {
        chatMsgMapper.updateListStatus(list);
    }

    @Override
    public List<ChatMsg> getUnReadMsg(String userId) {
        return chatMsgMapper.getUnReadMsg(userId);
    }
}
