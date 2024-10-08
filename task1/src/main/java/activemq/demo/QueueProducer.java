package activemq.demo;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.concurrent.TimeUnit;

public class QueueProducer {

    public static void main(String[] args) throws JMSException, InterruptedException {

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
        // 6. Create a message producer
        MessageProducer producer = session.createProducer(queue);
        // 7. Create a message
        TextMessage textMessage;
        String[] numbers_massage = {"Четыре", "Пять", "Шесть"};
        for (String s : numbers_massage){
            System.out.println("Отправляю сообщение!: "+s);
            textMessage = session.createTextMessage(s);
            // 8. Send message
            producer.send(textMessage);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        // 9. Close the resource
        producer.close();
        session.close();
        connection.close();
    }
}
