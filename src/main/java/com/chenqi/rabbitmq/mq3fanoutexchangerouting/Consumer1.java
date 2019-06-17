package com.chenqi.rabbitmq.mq3fanoutexchangerouting;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer1 {

    private static String QUEUE_NAME = "queue1";
    private static String EXCHANGE_NAME = "exchange-routing";

    public static void main(String[] args) {
        //创建链接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务主机
        factory.setHost("192.168.2.130");
        //设置登录名
        factory.setUsername("admin");
        //设置密码
        factory.setPassword("admin");
        //设置VirtualHost
        factory.setVirtualHost("/admin");
        Connection connection = null;
        try {
            //创建连接
            connection = factory.newConnection();
            //创建消息通道
            final Channel channel = connection.createChannel();
            //声明交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            //声明队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //绑定队列与交换机，指定接收的消息的Key
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "key2");
            //消息服务器每次只向消费者发送1条消息
            //channel.basicQos(1);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        Thread.sleep(1000);
                        String message = new String(body, "UTF-8");
                        System.out.println("consumer1 收到消息 '" + message + "'");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("consumer1 消息消费完成....");
                    }
                }
            };
            //监听消息
            channel.basicConsume(QUEUE_NAME, true, consumer);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
    }
}
