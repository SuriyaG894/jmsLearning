package jmsLearning.pubsub.app;

import jmsLearning.pubsub.model.Employee;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SecurityApp {
    public static void main(String args[]) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Topic topic = (Topic) initialContext.lookup("topic/empTopic");

        try(ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = activeMQConnectionFactory.createContext();){
            JMSConsumer consumer = jmsContext.createConsumer(topic);
            Message message = consumer.receive();
            Employee employee = (Employee) message.getBody(Employee.class);
            System.out.println(employee.getFirstName());
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
