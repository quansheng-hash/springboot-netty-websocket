package com.wqs.qctwe;

import com.wqs.qctwe.netty.WSServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author:wqs
 * @date:2018/11/8
 * @desciption:netty启动类
 */
@Component
public class NettyBootStrap implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 监听spring容器的刷新事件
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent()==null){
            //spring启动完后启动netty
            WSServer.getInstance().start();
        }
    }
}
