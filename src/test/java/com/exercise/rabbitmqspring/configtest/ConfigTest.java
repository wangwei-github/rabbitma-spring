package com.exercise.rabbitmqspring.configtest;

import com.exercise.rabbitmqspring.entity.Order;
import com.exercise.rabbitmqspring.entity.Packaged;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("spring.topic.queue", false, false, false)).
                to(new TopicExchange("spring.topic.exchange", false, false)).with("topic.#"));

        //链式编程fanout
        rabbitAdmin.declareExchange(new DirectExchange("spring.fanout.exchange", false, false));
        rabbitAdmin.declareQueue(new Queue("spring.fanout.queue", false, false, false));
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("spring.fanout.queue", false, false, false)).
                to(new FanoutExchange("spring.fanout.exchange", false, false)));

    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void template() {
        //创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("001", "第一次描述");
        messageProperties.getHeaders().put("002", "第二次描述");
        Message message = new Message("使用messageProperties发送消息！".getBytes(), messageProperties);

        //发送消息
        //	direct路由需要完全匹配
        //	topic可以模糊匹配
        rabbitTemplate.convertAndSend("exchange001", "spring001.*", message, message1 -> {
            message1.getMessageProperties().getHeaders().put("额外发送消息", "pretty");
            return message1;
        });
    }

    @Test
    public void template2() {
        //发送消息

        rabbitTemplate.convertAndSend("exchange002", "spring002.a", "交换机002");
        rabbitTemplate.convertAndSend("exchange002", "spring004.a", "交换机002，队列4");
    }

    @Test
    public void template3() {
        //发送消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        Message message = new Message("mq 消息 ".getBytes(), messageProperties);
        rabbitTemplate.convertAndSend("exchange001", "spring001.*", message);
        rabbitTemplate.convertAndSend("exchange002", "spring005.a", message);
    }

    @Test
    public void templateJsonConverter() throws JsonProcessingException {
        //发送消息
        Order order = new Order();
        order.setId("001");
        order.setName("json.convert");
        order.setDescription("商品描述001");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(order);
        System.err.println("order 4 json :"+json);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = new Message(json.getBytes(),messageProperties);

        rabbitTemplate.convertAndSend("exchange001", "spring001.*", message);

    }

    @Test
    public void templateJavaConverter() throws JsonProcessingException {
        //发送消息
        Order order = new Order();
        order.setId("002");
        order.setName("java.convert");
        order.setDescription("商品描述002");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(order);
        System.err.println("order 4 java :"+json);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        messageProperties.getHeaders().put("__TypeId__", "com.exercise.rabbitmqspring.entity.Order");
        Message message = new Message(json.getBytes(),messageProperties);

        rabbitTemplate.send("exchange001", "spring001.*", message);

    }
    @Test
    public void templateJavaMapperConverter() throws JsonProcessingException {
        //发送消息
        Order order = new Order();
        order.setId("003");
        order.setName("java.mapper.convert003");
        order.setDescription("商品描述003");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(order);
        System.err.println("order mapper java :"+json);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        messageProperties.getHeaders().put("__TypeId__", "order");
        Message message = new Message(json.getBytes(),messageProperties);

        rabbitTemplate.send("exchange001", "spring001.*", message);

        Packaged packaged = new Packaged();
        packaged.setId("004");
        packaged.setName("java.mapper.convert004");
        packaged.setContent("商品描述004");
        ObjectMapper objectMapper2 = new ObjectMapper();
        String json2 = objectMapper2.writeValueAsString(packaged);
        System.err.println("packaged mapper java :"+json2);

        messageProperties.getHeaders().put("__TypeId__", "packaged");
        Message message2 = new Message(json2.getBytes(),messageProperties);

        rabbitTemplate.send("exchange001", "spring001.*", message2);

    }

    @Test
    public void templateNIOPDFConverter()throws Exception{
        //发送消息
        byte[] bytes = Files.readAllBytes(Paths.get("E:/桌面","redis设计与实现.pdf"));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/pdf");
        Message message = new Message(bytes, messageProperties);

        rabbitTemplate.send("exchange001", "spring001.*", message);

    }
    @Test
    public void templateNIOImageConverter()throws Exception{
        //发送消息
        byte[] bytes = Files.readAllBytes(Paths.get("E:/桌面","机试题.jpg"));

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("extName", "jpg");
        messageProperties.setContentType("image");
        Message message = new Message(bytes, messageProperties);

        rabbitTemplate.send("exchange001", "spring001.*", message);

    }


}
