package com.exercise.rabbitmqspring.config;

import com.exercise.rabbitmqspring.delegate.ImageMessageConvert;
import com.exercise.rabbitmqspring.delegate.MessageDelegate;
import com.exercise.rabbitmqspring.delegate.PDFMessageConvert;
import com.exercise.rabbitmqspring.delegate.TextMessageConvert;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.UUID;

@Configuration
public class RabbitmqConfig {
   /* @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("192.168.11.129:5672");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("wangwei");
        connectionFactory.setPassword("123456");
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    *//*针对消费者配置
     * 设置交换机类型
     * 将队列绑定到交换机*//*
    @Bean
    public DirectExchange exchange001() {
        return new DirectExchange("exchange001", true, false);
    }

    @Bean
    public Queue queue001() {
        return new Queue("queue001", true);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring001.*");
    }

    @Bean
    public TopicExchange exchange002() {
        return new TopicExchange("exchange002", true, false);
    }

    @Bean
    public Queue queue002() {
        return new Queue("queue002", true);
    }

    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(queue002()).to(exchange002()).with("spring002.*");
    }

    @Bean
    public Queue queue004() {
        return new Queue("queue004", true);
    }

    @Bean
    public Queue queue005() {
        return new Queue("queue005", true);
    }

    @Bean
    public Binding binding3() {
        return BindingBuilder.bind(queue004()).to(exchange002()).with("spring004.*");
    }

    @Bean
    public Binding binding4() {
        return BindingBuilder.bind(queue005()).to(exchange002()).with("spring005.*");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        container.setQueues(queue001(), queue002(), queue004(), queue005());
        container.setDefaultRequeueRejected(false);//默认不重回队列
        container.setExposeListenerChannel(true);//显示通道
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);//自动签收
        container.setConsumerTagStrategy(queue -> queue + "_" + UUID.randomUUID().toString());//设置消费者标签策略
       *//* container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
          String msg = new String(message.getBody()) ;
            System.err.println("--------消费者："+msg);
            System.err.println("--------消费者："+message.getMessageProperties().getConsumerTag());
        });*//*

       //------------------------------------------

        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
       *//* HashMap<String,String> map = new HashMap<>();
        map.put("queue005", "method2");
        map.put("queue001", "method");
        adapter.setQueueOrTagToMethodName(map);
        adapter.setMessageConverter(new TextMessageConvert());*//*

        //支持json格式的转换器
       *//* Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        adapter.setMessageConverter(jackson2JsonMessageConverter);*//*

      *//*  //支持java对象转换Jackson2JsonMessageConverter & DefaultJackson2JavaTypeMapper
        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        //javaTypeMapper.setTrustedPackages("com.exercise");只能设置为*
        javaTypeMapper.setTrustedPackages("*");
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
        adapter.setMessageConverter(jackson2JsonMessageConverter);*//*

         //支持java对象多映射转换 Jackson2JsonMessageConverter & DefaultJackson2JavaTypeMapper
       *//* Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        HashMap<String,Class<?>>  javaClassMapper = new HashMap<>();
        javaClassMapper.put("order", com.exercise.rabbitmqspring.entity.Order.class);
        javaClassMapper.put("packaged", com.exercise.rabbitmqspring.entity.Packaged.class);
        javaTypeMapper.setIdClassMapping(javaClassMapper);
        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
        adapter.setMessageConverter(jackson2JsonMessageConverter);*//*

       //支持pdf file流转换器
        //全局转换器
        TextMessageConvert textMessageConvert = new TextMessageConvert();
        ContentTypeDelegatingMessageConverter contentConverter = new ContentTypeDelegatingMessageConverter();
        contentConverter.addDelegate("text",textMessageConvert);
        contentConverter.addDelegate("text/plain",textMessageConvert);
        contentConverter.addDelegate("html/text",textMessageConvert);
        contentConverter.addDelegate("xml/text",textMessageConvert);

        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        contentConverter.addDelegate("json", jackson2JsonMessageConverter);
        contentConverter.addDelegate("application/json", jackson2JsonMessageConverter);

        ImageMessageConvert imageMessageConvert = new ImageMessageConvert();
        contentConverter.addDelegate("image",imageMessageConvert);
        contentConverter.addDelegate("image/png",imageMessageConvert);

        PDFMessageConvert pdfMessageConvert = new PDFMessageConvert();
        contentConverter.addDelegate("application/pdf", pdfMessageConvert);
        adapter.setMessageConverter(contentConverter);

        //------------------------------------------
        container.setMessageListener(adapter);
        return container;
    }*/
}
