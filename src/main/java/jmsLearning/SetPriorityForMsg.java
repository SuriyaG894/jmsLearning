package jmsLearning;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;

public class SetPriorityForMsg {

    public static void main(String args[]) throws NamingException {

        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");

        try(ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = activeMQConnectionFactory.createContext();){
            JMSProducer producer = jmsContext.createProducer();
            String messages[] = new String[]{
                    "Message One",
                    "Message Two",
                    "Message Three"
            };
            producer.setPriority(3);
            producer.send(queue,messages[0]);

            producer.setPriority(9);
            producer.send(queue,messages[1]);

            producer.setPriority(5);
            producer.send(queue,messages[2]);


            QueueBrowser queueBrowser = jmsContext.createBrowser(queue);
            Enumeration enumeration = queueBrowser.getEnumeration();
            while(enumeration.hasMoreElements()){
                Message message = (Message) enumeration.nextElement();
                System.out.println(message.getBody(String.class));
//                System.out.println("Hi");
            }
            JMSConsumer consumer = jmsContext.createConsumer(queue);
            for(int i=0;i<3;i++){
                Message message = consumer.receive();
                System.out.println(message.getJMSPriority());
                System.out.println(message.getBody(String.class));
            }

            System.out.println("bye");
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

    }
}
