package jmsLearning.jmsSecurity;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EligibilityCheckApp {
    public static void main(String args[]) throws NamingException {
        InitialContext initialContext =  new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/srequestQueue");
        try(ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = activeMQConnectionFactory.createContext("eligibilityUser","eligibilityPass");){
            JMSConsumer consumer = jmsContext.createConsumer(queue);
            TextMessage message  = (TextMessage) consumer.receive(10000);
            System.out.println(message.getText());
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
