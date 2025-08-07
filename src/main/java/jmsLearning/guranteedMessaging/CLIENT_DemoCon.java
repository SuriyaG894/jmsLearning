package jmsLearning.guranteedMessaging;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CLIENT_DemoCon {
    public static void main(String args[]) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/requestQueue");

        try(ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = activeMQConnectionFactory.createContext(JMSContext.CLIENT_ACKNOWLEDGE);)
        {
            JMSConsumer consumer = jmsContext.createConsumer(queue);
            TextMessage textMessage = null;
            for(int i=0;i<3;i++) {
                textMessage = (TextMessage) consumer.receive(5000);
                System.out.println(textMessage.getText());
            }
            Thread.sleep(2000);
            textMessage.acknowledge();

        } catch (JMSException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
