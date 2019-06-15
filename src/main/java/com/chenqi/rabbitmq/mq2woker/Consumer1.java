package com.chenqi.rabbitmq.mq2woker;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者1
 */
public class Consumer1 {
    /**
     * 队列名字
     */
    private static final String QUEUE_NAME = "worker-queue";

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
        try {
            //创建连接
            connection = factory.newConnection();
            //创建消息通道
            final Channel channel = connection.createChannel();
            //声明队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //设置能者多劳模式
            //channel.basicQos(1);
            Consumer consumer = new DefaultConsumer(channel) {
                //重写DefaultConsumer中handleDelivery方法，在方法中获取消息
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        //这里模拟耗时操作 收到消息时沉睡一秒
                        Thread.sleep(1000);
                        String message = new String(body, "UTF-8");
                        System.out.println("consumer1 收到消息 '" + message + "'");

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("consumer1 消息消费完成....");
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };

            //监听消息
            channel.basicConsume(QUEUE_NAME, false, consumer);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

}
