package jmsLearning.p2p.hm.app;

import jmsLearning.p2p.hm.model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ClinicalApp {

    public static void main(String args[]) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
        Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
        try(ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = activeMQConnectionFactory.createContext();)
        {
            JMSProducer producer = jmsContext.createProducer();
            ObjectMessage objectMessage = jmsContext.createObjectMessage();
            Patient patient = new Patient();
            patient.setId(101);
            patient.setName("Suriya Ganesh");
            patient.setCopay(1500d);
            patient.setAmountToBePayed(500d);
            patient.setInsuranceProvider("Blue cross BluePrism");
            objectMessage.setObject(patient);
            producer.send(requestQueue,patient);

            JMSConsumer consumer = jmsContext.createConsumer(replyQueue);
            Message message = consumer.receive(30000);
            System.out.println(message.getBody(String.class));

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
