package activemq.demo;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

//Message consumer
public class QueueConsumer {

    public static void main(String[] args) throws Exception {
        // 1. Create a connection factory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://10.224.23.161:61616");
        // 2. Get the connection
        Connection connection = connectionFactory.createConnection();
        // 3. Start the connection
        connection.start();
        // 4. Get session (session object) parameter one: whether to start a transaction; parameter two: message confirmation method
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 5. Create a queue object
        Queue queue = session.createQueue("test-queue");
        // 6. Create a message consumer object
        MessageConsumer consumer = session.createConsumer(queue);
        // 7. Set monitor message
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("Message extracted:" + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        // 8. Wait for keyboard input
        System.in.read();
        // 9. Close the resource
        consumer.close();
        session.close();
        connection.close();
    }
}
