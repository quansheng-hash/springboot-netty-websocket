package com.wqs.qctwe.controller;

import com.wqs.qctwe.Res.BaseResponse;
import com.wqs.qctwe.domain.User;
import com.wqs.qctwe.dto.SearchFriendsDTO;
import com.wqs.qctwe.enums.FriendStatusEnum;
import com.wqs.qctwe.service.UserService;
import com.wqs.qctwe.util.FileUtils;
import com.wqs.qctwe.util.UUIDUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * @author:wqs
 * @date:2018/11/3
 * @desciption:
 */
@Api(value = "用户接口",description = "用户相关接口",tags = "UserController")
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;
    @Value("${filePath}")
    private String filePath;

    @ApiOperation(value = "用户注册接口")
    @RequestMapping("/register")
    public BaseResponse register(User user){
        int register = userService.register(user);
        if (register==1){
            return BaseResponse.successToClient(null);
        }else if(register==2){
            return BaseResponse.failToClient("用户名已存在");
        }
        return BaseResponse.failToClient("注册失败");
    }

    @ApiOperation(value = "用户登录")
    @RequestMapping("/login")
    public BaseResponse login(User user){
        User loginUser = userService.login(user);
        return loginUser==null?BaseResponse.failToClient("用户名或密码错误"):BaseResponse.successToClient(loginUser);
    }

    @ApiOperation("修改用户昵称接口")
    @RequestMapping("/updateNickname")
    public BaseResponse updateNickname(User user){
        User user1 = userService.updateNickname(user);
        if (user1==null){
            return BaseResponse.failToClient("修改昵称失败");
        }
        return BaseResponse.successToClient(user1);
    }

    @ApiOperation("用户头像上传")
    @RequestMapping("/upload")
    public BaseResponse fileUpLoad(MultipartFile file,String userId){
        String originalFilename = file.getOriginalFilename();
        String fileName= UUIDUtil.getUUID()+originalFilename.substring(originalFilename.lastIndexOf("."));
        try {
            FileUtils.savePic(file.getInputStream(), fileName,filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user=new User();
        user.setUserId(userId);
        user.setImgUrl(filePath+fileName);
        User user1 = userService.updateNickname(user);
        return BaseResponse.successToClient(user1);
    }
    @ApiOperation("根据账号查询添加好友")
    @RequestMapping("seachFriend")
    public BaseResponse seachFriend(String userId,String userName){
        SearchFriendsDTO searchFriendsDTO = userService.searchFriendsByUsername(userId, userName);
        if (searchFriendsDTO.getMsg().equals(FriendStatusEnum.USER_NOTEXIST.getMsg())){
            return BaseResponse.failToClient(FriendStatusEnum.USER_NOTEXIST.getMsg());
        }
        return BaseResponse.successToClient(searchFriendsDTO);
    }

    @ApiOperation("添加好友请求")
    @RequestMapping("addFriendRequest")
    public BaseResponse addFriendRequest(String sendUserId,String acceptUserId){
        int i = userService.addFriendRequest(sendUserId, acceptUserId);
        if (i==1){
            return BaseResponse.successToClient(null);
        }
        return BaseResponse.failToClient(null);
    }
    @ApiOperation("查看好友请求列表")
    @RequestMapping("getRequestList")
    public BaseResponse getRequestList(String userId){
        return BaseResponse.successToClient(userService.getRequestList(userId));
    }

    @ApiOperation("通过或者忽略好友的请求")
    @RequestMapping("psssOrIngoreRequest")
    public BaseResponse psssOrIngoreRequest(String acceptUserId,String sendUserId,Integer type){
        int i = userService.friendRequestResult(acceptUserId, sendUserId, type);
        if (i==1){
            return BaseResponse.successToClient(userService.getFriendsListByUserId(acceptUserId));
        }
        return BaseResponse.failToClient(null);
    }
    @ApiOperation("获取好友列表")
    @RequestMapping("getFriendsList")
    public BaseResponse getFriendsList(String userId){
        return BaseResponse.successToClient(userService.getFriendsListByUserId(userId));
    }
    @ApiOperation("获取未读消息列表")
    @RequestMapping("getUnReadMsg")
    public BaseResponse getUnReadMsg(String userId){
        return  BaseResponse.successToClient(userService.getUnReadMsg(userId));
    }

}
