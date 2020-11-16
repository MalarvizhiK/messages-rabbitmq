 
import java.net.URISyntaxException;
 
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
 
import com.rabbitmq.jms.admin.RMQConnectionFactory;
 
public class JMSProducer {
    public static void main(String[] args) throws URISyntaxException, Exception {
        Connection connection = null;
        try {
        	RMQConnectionFactory factory = new RMQConnectionFactory();
		factory.useSslProtocol();

		factory.setUsername("xyz");
		factory.setPassword("xyz");
		factory.setVirtualHost("/");
		factory.setHost("4f2ac774-4408-4e28-9a1d-f15cd074e04b.bkvfu0nd0m8k95k94ujg.databases.appdomain.cloud");
		factory.setPort(30696);

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


