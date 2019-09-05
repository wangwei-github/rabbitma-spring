package com.exercise.rabbitmqspring.delegate;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.*;
import java.nio.file.Files;
import java.util.UUID;

public class PDFMessageConvert implements MessageConverter {

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        throw new MessageConversionException("convert  error !");
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        System.err.println("------------PDF MessageConvert----------------");
        Object _extName = message.getMessageProperties().getHeaders().get("extName");
        String extName = _extName == null ? "pdf" : _extName.toString();
        String fileName = UUID.randomUUID().toString();
        byte[] body = message.getBody();
        String dirName = "e:/001_test";
        File file1 = new File(dirName);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        String path = fileName + "." + extName;
        File file = new File(dirName,path);
        try {
            Files.copy(new ByteArrayInputStream(body), file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
