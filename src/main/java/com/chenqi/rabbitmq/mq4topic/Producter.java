package com.chenqi.rabbitmq.mq4topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producter {

    private static final String EXCHANGE_NAME = "exchange-topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建链接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.2.130");
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("/admin");

        Connection connection = null;
        Channel channel = null;

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            String message = "hello world";
            //发送绑定消息
            channel.basicPublish(EXCHANGE_NAME,"key.one",null, ("one -" + message).getBytes());
            channel.basicPublish(EXCHANGE_NAME, "key.two.msg", null, ("two -" + message).getBytes());

        }catch (Exception e){

        }finally {
            channel.close();
            connection.close();
        }

    }
}
