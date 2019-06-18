package com.chenqi.rabbitmq.mq4topic;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 这里使用了key.#表示匹配所有key.下的所有路径，所以能够接收到所有以key开头的消息，控制台打印如下：
 * consumer2 收到消息 'one -Hello World!'
 * consumer2 消息消费完成....
 * consumer2 收到消息 'two -Hello World!'
 * consumer2 消息消费完成....
 *
 * 使用topic模式既可以轻易的实现fanout模式，也可以实现routing模式，同时提供了通配符的情况下，使得匹配更加灵活，使用方式更加简洁
 */
public class Consumer2 {
    /**
     * 队列名字
     */
    private static String QUEUE_NAME = "queue-topic-2";
    private static final String EXCHANGE_NAME = "exchange-topic";
    public static void main(String[] args){
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务器主机
        factory.setHost("127.0.0.1");
        //设置用户名
        factory.setUsername("wangx");
        //设置密码
        factory.setPassword("wangx");
        //设置VirtualHost
        factory.setVirtualHost("/wangx");
        Connection connection = null;
        try {
            //创建连接
            connection = factory.newConnection();
            //创建消息通道
            final Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            //声明队列
            channel.queueDeclare(QUEUE_NAME,false, false, false, null);
            //这里使用#号通配符，表示能够匹配到key下的任意路径
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "key.#");
            Consumer consumer = new DefaultConsumer(channel){
                //重写DefaultConsumer中handleDelivery方法，在方法中获取消息
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        //消息沉睡100ms
                        Thread.sleep(100);
                        String message = new String(body, "UTF-8");
                        System.out.println("consumer2 收到消息 '" + message + "'");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        System.out.println("consumer2 消息消费完成....");
                    }

                }
            };
            //监听消息，第二个参数为true时表示自动确认
            channel.basicConsume(QUEUE_NAME, true,consumer);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
        }
    }
}
