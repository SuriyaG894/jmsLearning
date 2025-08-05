package jmsLearning;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FullSafeCodeToSendAndReceiveMsg {
    public static void main(String args[]) throws NamingException, JMSException, InterruptedException {
        Context context = new InitialContext();

        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Queue queue = (Queue) context.lookup("queue/myQueue");

        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Send
        MessageProducer producer = session.createProducer(queue);
        TextMessage message = session.createTextMessage("Hello JMS");
        producer.send(message);
        System.out.println("Message sent.");

        Thread.sleep(5000L);
        // Receive
        MessageConsumer consumer = session.createConsumer(queue);
        TextMessage received = (TextMessage) consumer.receive(5000);

        if (received != null) {
            System.out.println("Received: " + received.getText());
        } else {
            System.out.println("No message received.");
        }

        connection.close();
        context.close();
    }



}