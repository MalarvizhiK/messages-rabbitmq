 
import java.net.URISyntaxException;
 
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import java.util.Properties;
import java.io.FileReader;
 
import com.rabbitmq.jms.admin.RMQConnectionFactory;
 
public class JMSProducer {
    public static void main(String[] args) throws URISyntaxException, Exception {
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
            	Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            	Queue queue = session.createQueue("jmsqueue");
            	MessageProducer producer = session.createProducer(queue);
            	String task = "Task";
            	for (int i = 0; i < 10; i++) {
                	String payload = task + i;
                	Message msg = session.createTextMessage(payload);
                	System.out.println("Sending text '" + payload + "'");
                	producer.send(msg);
            	}
            	producer.send(session.createTextMessage("END"));
		if (session != null) {
            		session.close();
		}
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}


