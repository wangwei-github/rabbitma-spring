package com.exercise.rabbitmqspring.config;

import java.io.File;

public class a {
    public static void main(String[] args) throws Exception{
       /* String path = "e:/001_test/111.png";
        File file = new File(path);
        file.createNewFile();*/
        File file=new File("e:\\3edfcbce92261570a138c4db4f2bf630\\222\\11.txt");
        if(!file.exists()){
            file.createNewFile();//创建该文件//n" java.io.IOException: 系统找不到指定的路径。
            System.out.println("文件以创建");
        }else{
            System.out.println("该文件已存在");
        }

    }
}
