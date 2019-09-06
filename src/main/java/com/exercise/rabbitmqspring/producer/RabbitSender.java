package com.exercise.rabbitmqspring.producer;

import com.entity.rabbitmq.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 消息确认机制  ack true/false 分别处理
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
        System.err.println("correlationData : " + correlationData);
        System.err.println("ack : " + ack);
        if (!ack) {
            System.err.println("发送异常");
        }
        System.err.println("cause : " + cause);
    };
    /**
     *消息返回机制  消息没有被路由，就反回，需要mandatory设置为true，不让消息直接删除
     */
    final RabbitTemplate.ReturnCallback returnCallback = (message, replyCode, replyText,
                                           exchange, routingKey) -> {
        System.err.println("message :" + message);
        System.err.println("replyCode :" + replyCode);
        System.err.println("replyText :" + replyText);
        System.err.println("exchange ：" + exchange);
        System.err.println("routingKey ：" + routingKey);
    };

    public void send(Object message, Map<String, Object> properties){
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message, messageHeaders);
        System.err.println("payload:"+msg.getPayload());
        String id = UUID.randomUUID().toString()+"_"+new Date().getTime();
        CorrelationData correlationData = new CorrelationData(id);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        //rabbitTemplate.convertAndSend("exchange-1","springboot.hello",msg,correlationData);
        rabbitTemplate.convertAndSend("exchange-1","lspringboot.hello",msg,correlationData);

    }

    @Value("${spring.rabbitmq.listener.order.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.listener.order.routing-key}")
    private String routingKey;
    public void  sendOrder(Order order){
        String id = UUID.randomUUID().toString()+"_"+new Date().getTime();
        CorrelationData correlationData = new CorrelationData(id);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        rabbitTemplate.convertAndSend(exchange,
                routingKey,order,correlationData);
    }
}
