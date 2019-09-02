package com.exercise.rabbitmqspring.configtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigTest {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Test
    public void admin() {

        //一步步声明绑定
        rabbitAdmin.declareExchange(new DirectExchange("spring.direct.exchange", false, false));
        rabbitAdmin.declareQueue(new Queue("spring.direct.queue", false, false, false));
        rabbitAdmin.declareBinding(new Binding("spring.direct.queue",
                Binding.DestinationType.QUEUE, "spring.direct.exchange",
                "direct.#", new HashMap<>()));

        //链式编程topic
        //  该版本amqp-client采用的是5.0.0以上,需要先声明交换机、队列下面binding才能运行成功。
        rabbitAdmin.declareExchange(new DirectExchange("spring.topic.exchange", false, false));
        rabbitAdmin.declareQueue(new Queue("spring.topic.queue", false, false, false));
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("spring.topic.queue",false,false,false)).
                to(new TopicExchange("spring.topic.exchange",false,false)).with("topic.#"));

        //链式编程fanout
        rabbitAdmin.declareExchange(new DirectExchange("spring.fanout.exchange", false, false));
        rabbitAdmin.declareQueue(new Queue("spring.fanout.queue", false, false, false));
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("spring.fanout.queue",false,false,false)).
                to(new FanoutExchange("spring.fanout.exchange",false,false)));

    }

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void template(){
        //创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("001", "第一次描述");
        messageProperties.getHeaders().put("002", "第二次描述");
        Message message = new Message("使用messageProperties发送消息！".getBytes(), messageProperties);

        //发送消息
        //	direct路由需要完全匹配
        //	topic可以模糊匹配
        rabbitTemplate.convertAndSend("exchange001","spring001.*",message, message1 -> {
            message1.getMessageProperties().getHeaders().put("额外发送消息", "pretty");
            return message1;
        });
    }
    @Test
    public void template2(){


        //发送消息
        rabbitTemplate.convertAndSend("exchange002","spring002.a","交换机002");
        rabbitTemplate.convertAndSend("exchange002","spring004.a","交换机002，队列4");
    }
}
