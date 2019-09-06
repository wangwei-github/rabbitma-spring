package com.exercise.rabbitmqspring.configtest.springboottest;

import com.entity.rabbitmq.Order;
import com.exercise.rabbitmqspring.producer.RabbitSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private RabbitSender rabbitSender;

    private SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/DD HH:mm:ss sss");

    @Test
    public  void testSend(){
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("num", "123456");
        properties.put("send_time", sdf.format(new Date()));
        rabbitSender.send("Hello SpringBoot RabbitMQ! ",properties);
    }

    @Test
    public void sendOrder() {
        Order order = new Order();
        order.setDescription("非常实用的汽车");
        order.setId("001100");
        order.setName("奥迪");
        rabbitSender.sendOrder(order);
    }


}
