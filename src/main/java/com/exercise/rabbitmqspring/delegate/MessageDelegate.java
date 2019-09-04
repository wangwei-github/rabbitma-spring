package com.exercise.rabbitmqspring.delegate;

import com.exercise.rabbitmqspring.entity.Order;
import com.exercise.rabbitmqspring.entity.Packaged;

import java.util.Map;

public class MessageDelegate {
    public void handleMessage(byte[] messageBody1) {
        System.err.println("handleMessage byte[]: " + new String(messageBody1));
    }

    public void consumeMessage(byte[] messageBody) {
        System.err.println("consumeMessage byte[]: " + new String(messageBody));
    }

    public void consumeMessage(String messageBody) {
        System.err.println("consumeMessage string: " + messageBody);
    }

    public void method(String messageBody) {
        System.err.println("method string: " + messageBody);
    }

    public void method2(String messageBody) {
        System.err.println("method2 string: " + messageBody);
    }

    public void consumeMessage(Map messageBody) {
        System.err.println("consumeMessage map: " + messageBody);
    }

    public void consumeMessage(Order messageBody) {
        System.err.println("consumeMessage order: " + messageBody);
    }

    public void consumeMessage(Packaged messageBody) {
        System.err.println("consumeMessage packaged: " + messageBody);
    }
}
