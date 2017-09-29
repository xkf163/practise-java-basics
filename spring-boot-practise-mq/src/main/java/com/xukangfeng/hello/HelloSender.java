package com.xukangfeng.hello;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * Created by F on 2017/8/3.
 */

@Component
@Controller
@RequestMapping(value = "/sender")
public class HelloSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @RequestMapping(value = "/1")
    public void sender(){
        System.out.println("Sender1: "+"hello message"+new Date());
        this.amqpTemplate.convertAndSend("hello","hello message"+new Date());
    }

}
