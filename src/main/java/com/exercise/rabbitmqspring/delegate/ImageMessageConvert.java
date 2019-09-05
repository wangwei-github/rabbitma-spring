package com.exercise.rabbitmqspring.delegate;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

public class ImageMessageConvert implements MessageConverter {
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        throw new MessageConversionException("convert  error !");
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        System.err.println("------------Image MessageConvert----------------");
        Object _extName = message.getMessageProperties().getHeaders().get("extName");
        String extName = _extName == null ? "png" : _extName.toString();

        byte[] body = message.getBody();
        String fileName = UUID.randomUUID().toString();

        //先创建目录才能创建文件
        String dirName = "e:\\001_test";
        File dir = new File(dirName);
        if (!dir.exists()){
            dir.mkdirs();
        }
        String filePath = fileName+"."+extName;
       // File file = new File(dir+"\\"+filePath);
        File file = new File(dir,filePath);
        System.err.println("6666"+file.toString());
        try {
            Files.copy(new ByteArrayInputStream(body), file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
