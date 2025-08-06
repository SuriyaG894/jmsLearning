package jmsLearning.pubsub.app;

import jmsLearning.pubsub.model.Employee;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class HRApp {
    public static void main(String args[]) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Topic topic = (Topic) initialContext.lookup("topic/empTopic");

        try(ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = activeMQConnectionFactory.createContext();){
            JMSProducer producer = jmsContext.createProducer();
            Employee employee = new Employee();
            employee.setId(101);
            employee.setFirstName("Suriya Ganesh");
            employee.setLastName("Venkatesan");
            employee.setDesignation("Software Engineer");
            employee.setEmail("suriyag894@gmail.com");
            producer.send(topic,employee);
            System.out.println("Message Sent");
        }
    }
}
