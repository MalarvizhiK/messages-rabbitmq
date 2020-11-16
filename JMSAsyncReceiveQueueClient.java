 
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
 
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.rabbitmq.jms.admin.RMQConnectionFactory;

public class JMSAsyncReceiveQueueClient {
    private CountDownLatch latch = new CountDownLatch(1);
    public static void main(String[] args) throws URISyntaxException, Exception {
        JMSAsyncReceiveQueueClient asyncReceiveClient = new JMSAsyncReceiveQueueClient();
        asyncReceiveClient.receiveMessages();
    }
 
    public void receiveMessages() throws JMSException, InterruptedException, Exception {
        Connection connection = null;

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
        try {
            Queue queue = session.createQueue("jmsqueue");
 
            // Consumer
            MessageConsumer consumer = session.createConsumer(queue);
            ConsumerMessageListener consumerListener = new ConsumerMessageListener(
                    "Customer");
            consumer.setMessageListener(consumerListener);
            consumerListener.setAsyncReceiveQueueClientExample(this);
         
            connection.start();
            latch.await();
        } finally {
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        }       
    }
 
    public void latchCountDown() {
        latch.countDown();
    }
}

class ConsumerMessageListener implements MessageListener {
    private String consumerName;
    private JMSAsyncReceiveQueueClient asyncReceiveQueueClientExample;

    public ConsumerMessageListener(String consumerName) {
        this.consumerName = consumerName;
    }

    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println(consumerName + " received " + textMessage.getText());
            if ("END".equals(textMessage.getText())) {
                asyncReceiveQueueClientExample.latchCountDown();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void setAsyncReceiveQueueClientExample(
            JMSAsyncReceiveQueueClient asyncReceiveQueueClientExample) {
        this.asyncReceiveQueueClientExample = asyncReceiveQueueClientExample;
    }
}

