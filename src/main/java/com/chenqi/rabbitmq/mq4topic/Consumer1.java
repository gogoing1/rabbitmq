package com.chenqi.rabbitmq.mq4topic;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 上面生产者发送消息时，绑定了key.one和key.two.msg两种模式。接下来使用*号和#号两种通配符得消费者，如下：
 * Consumer1
 *
 *
 * 所以在本例中只能匹配到key.one的消息，控制台打印如下：
 * consumer1 收到消息 'one -Hello World!'
 * consumer1 消息消费完成....
 */
public class Consumer1 {

    /**
     * 队列名字
     */
    private static String QUEUE_NAME = "queue-topic";
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
            //声明交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            //声明队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //绑定队列与交换机,使用通配符key.* 表示只能匹配key下的一个路径，
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "key.*");
            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        //消息沉睡一秒
                        Thread.sleep(1000);
                        String message = new String(body, "UTF-8");
                        System.out.println("consumer1 收到消息 '" + message + "'");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        System.out.println("consumer1 消息消费完成....");
                    }
                }
            };

            //监听消息
            channel.basicConsume(QUEUE_NAME, true,consumer);

        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }

    }







}
