package com.chenqi.rabbitmq.mq2woker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息生产者
 * 这里发送了10条消息到消息队列里面
 */
public class Producer {
    /**
     * 队列名字
     */
    private static final String QUEUE_NAME = "worker-queue";

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
            //创建连接
            connection = factory.newConnection();
            //创建消息通道
            channel = connection.createChannel();
            //声明队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //发送消息
            String message = "hello world!";
            for (int i = 0; i < 10; i++) {
                channel.basicPublish("", QUEUE_NAME, null, (message + i).getBytes());
                System.out.println(" [x] Sent '" + message + i + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            channel.close();
            connection.close();
        }

    }


}
