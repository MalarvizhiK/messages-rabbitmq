import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import java.util.Properties;
import java.io.FileReader;

import com.rabbitmq.jms.admin.RMQConnectionFactory;

public class JMSPublisherClient {
    public static void main(String[] args) throws Exception {
        Connection connection = null;
        try {

	// read the properties from connection.properties
	FileReader reader=new FileReader("connection.properties");  
      
    	Properties p=new Properties();  
    	p.load(reader);  
      
        // Producer
	RMQConnectionFactory factory = new RMQConnectionFactory();
        factory.useSslProtocol();

        factory.setUsername(p.getProperty("username"));
        factory.setPassword(p.getProperty("password"));

	factory.setVirtualHost(p.getProperty("virtualhost"));
        factory.setHost(p.getProperty("hostname"));
        factory.setPort(Integer.parseInt(p.getProperty("port")));

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
