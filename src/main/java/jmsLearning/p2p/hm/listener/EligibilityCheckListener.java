package jmsLearning.p2p.hm.listener;

import jmsLearning.p2p.hm.model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EligibilityCheckListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try(ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        JMSContext jmsContext = activeMQConnectionFactory.createContext();){
            Patient patient = (Patient) objectMessage.getObject();
            InitialContext initialContext = new InitialContext();
            Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
            JMSProducer producer = jmsContext.createProducer();
            if(patient.getCopay()<1500 && patient.getInsuranceProvider().equalsIgnoreCase("Blue Cross BluePrism") ){
                producer.send(replyQueue,patient.getName()+" is not eligible");
            }
            else {
                producer.send(replyQueue,patient.getName()+" is eligible");
            }
        }
        catch (JMSException e){
            e.printStackTrace();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
