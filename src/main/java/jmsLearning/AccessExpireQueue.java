package jmsLearning;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class AccessExpireQueue {
    public static void main(String args[]) throws NamingException {
        InitialContext context = new InitialContext();
        Queue queue = (Queue) context.lookup("queue/myQueue");

        try (ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = activeMQConnectionFactory.createContext()) {

            JMSProducer producer = jmsContext.createProducer();
            producer.setTimeToLive(1000); // 1 second
            producer.send(queue, "Hi, I'm Suriya Ganesh");
            System.out.println("Message sent with TTL = 1 sec");

            // Wait 3 seconds so message expires
            Thread.sleep(3000);

            JMSConsumer consumer = jmsContext.createConsumer(queue);
            Message messageReceived = consumer.receive(1000); // timeout of 1 sec

            if (messageReceived == null) {
                System.out.println("Message expired from myQueue");

                Queue expiryQueue = (Queue) context.lookup("queue/expiryQueue");
                JMSConsumer consumerAccessExpiry = jmsContext.createConsumer(expiryQueue);
                Message expiredMessage = consumerAccessExpiry.receive(5000); // wait max 5 seconds

                if (expiredMessage != null) {
                    System.out.println("Retrieved from expiry queue: " + expiredMessage.getBody(String.class));
                } else {
                    System.out.println("No message in expiry queue.");
                }
            } else {
                System.out.println("Message Received before expiration: " + messageReceived.getBody(String.class));
            }

        } catch (JMSException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
