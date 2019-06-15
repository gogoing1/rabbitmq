package com.chenqi.rabbitmq.mq1hello;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MyConsumer {
    /**
     * 队列名字
     */
    private static final String QUEUE_NAME = "MyQueue";
    public static void main(String[] args) throws IOException, TimeoutException {

        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务器主机
        factory.setHost("192.168.2.130");
        //设置用户
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
            Consumer consumer = new DefaultConsumer(channel){
                //重写DefaultConsumer中handleDelivery方法，在方法中获取消息
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException{
                    String message = new String(body, "UTF-8");
                    System.out.println("收到消息 '" + message + "'");
                }
            };
            //监听消息
            channel.basicConsume(QUEUE_NAME, true,consumer);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
        }
    }
}
