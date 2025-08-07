package jmsLearning.guranteedMessaging;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQMessageConsumer;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SESSION_DemoCon {
    public static void main(String args[]) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/requestQueue");

        try(ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = activeMQConnectionFactory.createContext(JMSContext.SESSION_TRANSACTED);)
        {
            JMSConsumer consumer1 = jmsContext.createConsumer(queue);
            TextMessage message = (TextMessage) consumer1.receive(5000);
            System.out.println(message.getText());
            Thread.sleep(2000);
            JMSConsumer consumer2 = jmsContext.createConsumer(queue);
            TextMessage message2 = (TextMessage) consumer2.receive(5000);
            System.out.println(message2.getText());

            jmsContext.commit();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
