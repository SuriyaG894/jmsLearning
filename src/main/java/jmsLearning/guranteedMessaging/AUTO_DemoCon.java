package jmsLearning.guranteedMessaging;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class AUTO_DemoCon {
    public static void main(String args[]) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/requestQueue");

        try(ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = activeMQConnectionFactory.createContext();){

            JMSConsumer consumer = jmsContext.createConsumer(queue);
            System.out.println(consumer.receive().getBody(String.class));

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

    }
}
