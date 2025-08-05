package jmsLearning;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class sendAndReceiveMsg {
    public static void main(String args[]){
        try {
            // Initialize the context
            InitialContext initialContext = new InitialContext();
            // Initialize the connectionFactory
            ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            // Creating Connection
            Connection connection = connectionFactory.createConnection();
            // Creating Session
            Session session = connection.createSession();
            // Getting the destination Queue
            Queue queue = (Queue) initialContext.lookup("queue/myQueue");
            // Creating Message Producer
            MessageProducer messageProducer = session.createProducer(queue);
            // Creating Test message
            TextMessage textMessage = session.createTextMessage("I am Suriya Ganesh.");
            // Sending Text Message
            messageProducer.send(textMessage);

            MessageConsumer messageConsumer = session.createConsumer(queue);
            connection.start();
            TextMessage textMessage1 = (TextMessage) messageConsumer.receive(10000L);
            System.out.println(textMessage1.getText());

            connection.close();
            initialContext.close();

        } catch (NamingException e) {
            throw new RuntimeException(e);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }


    }
}
