package com.exercise.rabbitmqspring.configtest;

import org.junit.Test;
import org.junit.runner.RunWith;
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
}
