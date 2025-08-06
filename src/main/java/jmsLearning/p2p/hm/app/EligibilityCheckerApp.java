package jmsLearning.p2p.hm.app;

import jmsLearning.p2p.hm.listener.EligibilityCheckListener;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.jms.Queue;

public class EligibilityCheckerApp {
    public static void main(String args[]) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

        try(ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = activeMQConnectionFactory.createContext();){
            JMSConsumer consumer =  jmsContext.createConsumer(requestQueue);
            consumer.setMessageListener(new EligibilityCheckListener());

            Thread.sleep(10000);
        } catch (InterruptedException e) {

        }
    }
}
