package jmsLearning.pubsub.app;

import jmsLearning.pubsub.model.Employee;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class DurableSubscriptionExample {
    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext initialContext = new InitialContext();
        Topic topic = (Topic) initialContext.lookup("topic/empTopic");

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {

            // Set the client ID before creating any consumers
            jmsContext.setClientID("checkDurableSub");

            // Create a durable consumer (first time only)
            JMSConsumer consumer = jmsContext.createDurableConsumer(topic, "subscription1");
            consumer.close(); // Simulate the consumer going offline

            System.out.println("Consumer closed. Waiting 10 seconds...");
            Thread.sleep(10000);

            // Simulate messages being sent to the topic during this time
            // They will be retained for the durable subscriber

            // Reconnect the durable subscriber
            JMSConsumer consumer1 = jmsContext.createDurableConsumer(topic, "subscription1");
            Message message = consumer1.receive(5000); // wait max 5 seconds

            if (message != null && message.isBodyAssignableTo(Employee.class)) {
                Employee emp = message.getBody(Employee.class);
                System.out.println("Received message for: " + emp.getFirstName());
            } else {
                System.out.println("No message received.");
            }

            consumer1.close();

            // Optionally unsubscribe the durable subscription
            jmsContext.unsubscribe("subscription1");

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
