package jmsLearning;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TopicDemo {

    public static void main(String args[]) throws NamingException, JMSException {
        InitialContext initialContext = new InitialContext();
        ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
        Connection connection  = connectionFactory.createConnection();

        Topic topic = (Topic) initialContext.lookup("topic/myTopic");
        Session session = connection.createSession();
        TextMessage textMessage = session.createTextMessage("This is topic demo (Pub Sub)");
        MessageProducer messageProducer = session.createProducer(topic);
        MessageConsumer consumer1 = session.createConsumer(topic);
        MessageConsumer consumer2 = session.createConsumer(topic);
        messageProducer.send(textMessage);

        connection.start();
        TextMessage message1 = (TextMessage) consumer1.receive(5000L);
        TextMessage message2 = (TextMessage) consumer2.receive(5000L);
        System.out.println("Message from received consumer1 :"+message1.getText());
        System.out.println("Message from received consumer1 :"+message2.getText());
        connection.close();
        initialContext.close();

    }
}
