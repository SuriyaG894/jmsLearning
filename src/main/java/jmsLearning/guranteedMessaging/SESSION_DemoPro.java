package jmsLearning.guranteedMessaging;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SESSION_DemoPro {
    public static void main(String args[]) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/requestQueue");

        try(ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = activeMQConnectionFactory.createContext(JMSContext.SESSION_TRANSACTED);){
            JMSProducer producer = jmsContext.createProducer();
            producer.send(queue,"Message 1");
            producer.send(queue,"Message 2");
            jmsContext.commit();
        }
    }
}
