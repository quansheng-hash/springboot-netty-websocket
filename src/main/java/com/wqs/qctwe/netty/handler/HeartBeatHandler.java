package com.wqs.qctwe.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author:wqs
 * @date:2018/11/12
 * @desciption:
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    /**
     * 用户事件监听
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event= (IdleStateEvent) evt;
            if (event.state()== IdleState.READER_IDLE){
                System.out.println("进入读空闲");
            }
             else if (event.state()==IdleState.WRITER_IDLE){
                System.out.println("进入写空闲");
            }
             else if (event.state()==IdleState.ALL_IDLE){
                System.out.println("进入读写空闲...关闭通道");
                ctx.channel().close();
            }
        }

    }
}
