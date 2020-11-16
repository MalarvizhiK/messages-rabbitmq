import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import com.rabbitmq.jms.admin.RMQConnectionFactory;

public class JMSPublisherClient {
    public static void main(String[] args) throws Exception {
        Connection connection = null;
        try {
            // Producer
	RMQConnectionFactory factory = new RMQConnectionFactory();
        factory.useSslProtocol();

        factory.setUsername("ibm_cloud_983e2c42_4865_4755_9c83_47967c72141e");
        factory.setPassword("36aa9ca8368b9dbbb3151782c4bbf46c0b7d6c9390cf1b11a36892ff25a1fb7e");

	factory.setVirtualHost("/");
        factory.setHost("4f2ac774-4408-4e28-9a1d-f15cd074e04b.bkvfu0nd0m8k95k94ujg.databases.appdomain.cloud");
        factory.setPort(30696);

        System.out.println("Created Connection Factory");

        connection = factory.createConnection();
        System.out.println("Created Connection");
            connection.setClientID("DurabilityTest");
            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("customerTopic");
 
            // Publish
            String payload = "Task";
            TextMessage msg = session.createTextMessage(payload);
            MessageProducer publisher = session.createProducer(topic);
            System.out.println("Sending text '" + payload + "'");
            publisher.send(msg, javax.jms.DeliveryMode.PERSISTENT, javax.jms.Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
 
            session.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
