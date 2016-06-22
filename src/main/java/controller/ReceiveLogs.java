package controller;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ReceiveLogs {

	private static final String EXCHANGE_NAME = "logs";

	/**
	 * Publish/Subscribe: Subscribe
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		final Channel channel = connection.createChannel();

		// 自动生成channel
		String queueName = channel.queueDeclare().getQueue();
		// queue绑定exchange
		channel.queueBind(queueName, EXCHANGE_NAME, "");
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String messages = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + messages + "'");
			};
		};
		channel.basicConsume(queueName, true, consumer);

		channel.close();
		connection.close();
	}

}
