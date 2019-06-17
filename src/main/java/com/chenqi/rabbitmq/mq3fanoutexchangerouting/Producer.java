package com.chenqi.rabbitmq.mq3fanoutexchangerouting;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者生产消息 发送消息时指定不同的key，交换机分发消息是根据key分发消息到不同的消息队列中
 * 1，将交换机模式改为DIRECT ，在消息端设置了不同的key，相当于把消息分个类。以便于在交换机分发消息时
 * 将消息发送给持有该key的消费者
 */
public class Producer {

    private static final String EXCHANGE_NAME = "exchange-routing";

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
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            String message = "hello world!";
            for (int i = 0; i < 30; i++) {
                if (i % 2 == 0) {
                    //偶数消息
                    channel.basicPublish(EXCHANGE_NAME, "key2", null, (message + i).getBytes());
                    System.out.println(" 偶数消息 '" + message + i + "'");
                }else {
                    //奇数消息
                    channel.basicPublish(EXCHANGE_NAME, "key1", null, (message + i).getBytes());
                    System.out.println(" 奇数消息 '" + message + i + "'");
                }
            }

        } catch (Exception e) {

        }finally {
            channel.close();
            connection.close();
        }

    }
}
