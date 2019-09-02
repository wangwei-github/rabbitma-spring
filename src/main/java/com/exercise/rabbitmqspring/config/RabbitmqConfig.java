package com.exercise.rabbitmqspring.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    @Bean
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

    /*针对消费者配置
    * 设置交换机类型
    * 将队列绑定到交换机*/

    @Bean
    public DirectExchange exchange001(){
        return new DirectExchange("exchange001", true, false);
    }
    @Bean
    public Queue queue001(){
        return new Queue("queue001", true);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring001.*");
    }

    @Bean
    public TopicExchange exchange002(){
        return new TopicExchange("exchange002", true, false);
    }
    @Bean
    public Queue queue002(){
        return new Queue("queue002", true);
    }

    @Bean
    public Binding binding2(){
        return BindingBuilder.bind(queue002()).to(exchange002()).with("spring002.*");
    }

    @Bean
    public Queue queue004(){
        return new Queue("queue004", true);
    }@Bean
    public Queue queue005(){
        return new Queue("queue005", true);
    }

    @Bean
    public Binding binding3(){
        return BindingBuilder.bind(queue004()).to(exchange002()).with("spring004.*");
    }
    @Bean
    public Binding binding4(){
        return BindingBuilder.bind(queue005()).to(exchange002()).with("spring005.*");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }
}
