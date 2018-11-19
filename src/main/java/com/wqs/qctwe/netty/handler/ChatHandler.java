package com.wqs.qctwe.netty.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wqs.qctwe.domain.ChatMsg;
import com.wqs.qctwe.enums.MsgSignFlagEnum;
import com.wqs.qctwe.enums.WebSocketActionEunm;
import com.wqs.qctwe.netty.dto.WebSocketChatMsg;
import com.wqs.qctwe.netty.dto.DataDTO;
import com.wqs.qctwe.netty.relation.UserChannelRelation;
import com.wqs.qctwe.service.UserService;
import com.wqs.qctwe.util.SpringUtil;
import com.wqs.qctwe.util.UUIDUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author:wqs
 * @date:2018/11/8
 * @desciption:
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 创建管理用户channel的组
     */
    private static ChannelGroup users=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        Channel channel = channelHandlerContext.channel();
        //获取用户消息
        String msg=textWebSocketFrame.text();
        //将消息转成对象
        ObjectMapper objectMapper=new ObjectMapper();
        DataDTO dataDTO = objectMapper.readValue(msg, DataDTO.class);
        //判断类型
        if (dataDTO.getAction()== WebSocketActionEunm.CONNECT.getAction()){
            // 建立连接类型  将用户id与channel对应起来
            String sendUserId=dataDTO.getChatMsg().getSendUserId();
            UserChannelRelation.put(sendUserId, channel);
        }else if(dataDTO.getAction()==WebSocketActionEunm.CHAT.getAction()){
            //聊天类型的信息 把聊天记录保持至数据库 设置消息的状态为未签收
            WebSocketChatMsg chatMsg=dataDTO.getChatMsg();
            //从spring容器中获取UserSeriver对象 把记录保存到对象中
            ChatMsg msg1=new ChatMsg();
            String id=UUIDUtil.getUUID();
            msg1.setId(id);
            chatMsg.setMsgId(id);
            msg1.setAcceptUserId(chatMsg.getAcceptUserId());
            msg1.setCreateTime(new Date());
            msg1.setSendUserId(chatMsg.getSendUserId());
            msg1.setMsg(chatMsg.getMsg());
            msg1.setSignFlag(MsgSignFlagEnum.unsign.type);
            UserService userService= (UserService) SpringUtil.getBean("userServiceImpl");
            userService.saveChatMsg(msg1);
            //将消息发送给接收方
            Channel acceptChannel = UserChannelRelation.get(chatMsg.getAcceptUserId());
            if (acceptChannel==null){
                // TODO channel为空代表用户离线，推送消息（JPush，个推，小米推送）
            }else{
                //当acceptChannel不为空时,从channel组中找到channel
                Channel channel1 = users.find(acceptChannel.id());
                if (channel1==null){
                    // 用户离线 TODO 推送消息
                }else{
                    //用户在线 发送消息
                    channel1.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(dataDTO)));
                }
            }

        }else if(dataDTO.getAction()==WebSocketActionEunm.SINGNED.getAction()){
            //签收类型   获取消息id
            List<String> list=new ArrayList<>();
            String extand=dataDTO.getExtand();
            String []msgIds=extand.split(",");
            for (String id:msgIds){
                if(StringUtils.isNotBlank(id)){
                    list.add(id);
                }
            }
            if (list!=null&&list.size()>0&&!list.isEmpty()){
                //进行批量签收
                UserService userService= (UserService) SpringUtil.getBean("userServiceImpl");
                userService.updateChatStatus(list);
            }
        }else if (dataDTO.getAction()==WebSocketActionEunm.KEEPALIVE.getAction()){
            System.out.println("收到来自channel为[" + channel + "]的心跳包...");
        }
        /*System.out.println("收到的消息为:"+msg);
        users.writeAndFlush(new TextWebSocketFrame("服务器回应"+msg));*/
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道连接---");
        users.add(ctx.channel());
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("移除---");

        super.handlerRemoved(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //发送异常时
        Channel channel = ctx.channel();
        channel.close();
        users.remove(channel);
        super.exceptionCaught(ctx, cause);
    }
}
