package jmsLearning.msgFilter.app;

import jmsLearning.msgFilter.model.Claim;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ClaimManagementApp {
    public static void main(String args[]) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/claimQueue");

        try(ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = activeMQConnectionFactory.createContext();)
        {
            JMSProducer producer = jmsContext.createProducer();
            Claim claim = new Claim();
            claim.setHospitalId(101);
            claim.setDoctorName("Pal");
            claim.setDoctorType("Heart Surgeon");
            claim.setInsuranceProvider("Bluecross");
            claim.setClaimAmount(4000d);
            ObjectMessage objectMessage = jmsContext.createObjectMessage();
            objectMessage.setObject(claim);
            objectMessage.setIntProperty("hospitalId",1);
            producer.setPriority(8);
            producer.send(queue,objectMessage);

//            JMSConsumer consumer = jmsContext.createConsumer(queue,"hospitalId=1");
            JMSConsumer consumer = jmsContext.createConsumer(queue,"JMSPriority BETWEEN 6 AND 9");
            Message receivedMessage = consumer.receive(5000);

            Claim receivedClaimObject = receivedMessage.getBody(Claim.class);
            System.out.println("Received Message :"+receivedClaimObject.getDoctorName());
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

    }
}
