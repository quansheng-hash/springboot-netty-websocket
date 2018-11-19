package com.wqs.qctwe.netty;

import com.wqs.qctwe.netty.handler.ChatHandler;
import com.wqs.qctwe.netty.handler.HeartBeatHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;


/**
 * @author:wqs
 * @date:2018/11/8
 * @desciption:
 */
public class WSServerInit extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        //webSocket基于http协议所以需要添加http模块
        channel.pipeline().addLast(new HttpServerCodec());
        //添加对大数据的读写支持
        channel.pipeline().addLast(new ChunkedWriteHandler());
        // 对httpMessage进行聚合，聚合成FullHttpRequest或FullHttpResponse
        // 几乎在netty中的编程，都会使用到此hanler
        channel.pipeline().addLast(new HttpObjectAggregator(1024*64));

        //增加心跳检测
        channel.pipeline().addLast(new IdleStateHandler(30,30,61));
        channel.pipeline().addLast(new HeartBeatHandler());

        //添加对webSocket的支持
        channel.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));
        //添加自定义的聊天业务支持
        channel.pipeline().addLast(new ChatHandler());


    }
}
