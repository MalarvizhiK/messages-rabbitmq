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

public class JMSSubscriberClient {
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
 
            // Consumer1 subscribes to customerTopic
            MessageConsumer consumer1 = session.createDurableSubscriber(topic, "consumer1", "", false);     
 
            // Consumer2 subscribes to customerTopic
            MessageConsumer consumer2 = session.createDurableSubscriber(topic, "consumer2", "", false); 
 
            connection.start();
             
            TextMessage msg = (TextMessage) consumer1.receive();
            System.out.println("Consumer1 receives " + msg.getText());
            //acknowledge
            msg.acknowledge(); 
             
            msg = (TextMessage) consumer2.receive();
            System.out.println("Consumer2 receives " + msg.getText());
 	    //acknowledge
            msg.acknowledge();	
            session.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
