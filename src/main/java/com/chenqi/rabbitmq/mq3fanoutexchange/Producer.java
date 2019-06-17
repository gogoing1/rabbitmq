package com.chenqi.rabbitmq.mq3fanoutexchange;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 一个生产者生产消息  进入到两个消费者的两个消费队列里面，两个消费者都能消费同样的消息
 */
public class Producer {

    private static final String EXCHANGE_NAME = "exchange";

    public static void main(String[] args) throws IOException, TimeoutException {

        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务器主机
        factory.setHost("192.168.2.130");
        //设置用户名
        factory.setUsername("admin");
        //设置密码
        factory.setPassword("admin");
        //设置VirtualHost
        factory.setVirtualHost("/admin");
        Connection connection = null;
        Channel channel = null;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            //声明交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            String message = "hello world!";
            for (int i = 0; i < 30; i++) {
                //发送消息
                channel.basicPublish(EXCHANGE_NAME, "", null, (message + i).getBytes());
                System.out.println(" [x] Sent '" + message + i + "'");
            }

        } catch (Exception e) {

        }finally {
            channel.close();
            connection.close();
        }

    }
}
