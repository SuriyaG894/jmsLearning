package jmsLearning.jmsSecurity;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ClinicalApp {
    public static void main(String args[]) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/srequestQueue");
        try(ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = activeMQConnectionFactory.createContext("clinicalUser","clinicalPass");)
        {
            JMSProducer consumer = jmsContext.createProducer();
            consumer.send(queue,"Message 1 from clinical app");
        }
    }
}
