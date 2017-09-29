package com.xukangfeng.hello;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by F on 2017/8/3.
 */

@RabbitListener(queues = "hello")
@Component
public class HelloReceiver{

    @RabbitHandler
    public void process(String message){
        System.out.println("receiver1: "+message);
    }

}
