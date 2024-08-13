package activemq.demo;
/**
 * для запуска с аргументом path:
 * mvn exec:java -Dexec.mainClass="activemq.demo.QueueProducer" -Dexec.args="/home/sonikx/IdeaProjects/task2/src/main/java/activemq/demo/file.txt
 */

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class QueueProducer {

    public static void main(String[] args) throws JMSException, InterruptedException, FileNotFoundException {

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
        Scanner scanner = null;
        //String path_from_terminal = System.out.println(args[0]);
        while (true) {
            try {
                scanner = new Scanner(new File(args[0]));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                System.out.println("Отправляю строку из фаила!: "+s);
                textMessage = session.createTextMessage(s);
                // 8. Send message
                producer.send(textMessage);
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            scanner.close();
        }

        // 9. Close the resource
        producer.close();
        session.close();
        connection.close();
    }
}
