package jmsLearning;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;

public class QueueBrowserDemo {

    public static void main(String args[]) throws NamingException, JMSException {
        InitialContext initialContext = new InitialContext();
        ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");
        Session session = connection.createSession();
        MessageProducer messageProducer = session.createProducer(queue);
        TextMessage textMessage1 = session.createTextMessage("Message 1");
        TextMessage textMessage2 = session.createTextMessage("Message 2");
        messageProducer.send(textMessage1);
        messageProducer.send(textMessage2);

        QueueBrowser queueBrowser = session.createBrowser(queue);
        Enumeration enumeration = queueBrowser.getEnumeration();
        while(enumeration.hasMoreElements()){
            TextMessage textMessage = (TextMessage) enumeration.nextElement();
            System.out.println("Text Message :"+textMessage.getText());
        }
        System.out.println("-------------------------------------------------------");

        connection.start();
        MessageConsumer messageConsumer = session.createConsumer(queue);
        TextMessage receivedMessage1 = (TextMessage) messageConsumer.receive();
        System.out.println(receivedMessage1.getText());
        TextMessage receivedMessage2 = (TextMessage) messageConsumer.receive();
        System.out.println(receivedMessage2.getText());
        connection.close();
        initialContext.close();
    }
}
