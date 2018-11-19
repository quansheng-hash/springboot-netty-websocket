package com.wqs.qctwe.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.stereotype.Component;

/**
 * @author:wqs
 * @date:2018/11/8
 * @desciption:netty服务类
 */
@Component
public class WSServer {

    //内部静态类保证WSServer实例是单例的
    private static class SingletionWSServer{
       static final WSServer instance=new WSServer();
    }

    public static WSServer getInstance(){
        return SingletionWSServer.instance;
    }
    private EventLoopGroup mainGroup;
    private EventLoopGroup workGroup;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;
    private WSServer(){
        mainGroup=new NioEventLoopGroup();
        workGroup=new NioEventLoopGroup();
        serverBootstrap=new ServerBootstrap();
        serverBootstrap.group(mainGroup,workGroup )
                .channel(NioServerSocketChannel.class)
                .childHandler(new WSServerInit());
    }
    public void start(){
     this.channelFuture=serverBootstrap.bind(7777);
     System.err.println("netty 启动完毕");
    }
}
