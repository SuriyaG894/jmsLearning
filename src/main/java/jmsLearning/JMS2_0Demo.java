package jmsLearning;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMS2_0Demo {
    public static void main(String args[]) throws NamingException {

        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");

        try(ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = activeMQConnectionFactory.createContext()){
            jmsContext.createProducer().send(queue,"JMS 2.0 Demo Check message");
            String message = jmsContext.createConsumer(queue).receiveBody(String.class);
            System.out.println(message);
        }
    }
}
