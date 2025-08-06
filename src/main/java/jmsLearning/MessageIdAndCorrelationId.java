package jmsLearning;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessageIdAndCorrelationId {
    public static void main(String args[]){
        try {
            InitialContext context = new InitialContext();
            Queue queue = (Queue) context.lookup("queue/myQueue");
            try(ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
                JMSContext jmsContext = activeMQConnectionFactory.createContext()){
                JMSProducer producer = jmsContext.createProducer();
                TextMessage message = jmsContext.createTextMessage("I am inevitable");
                TemporaryQueue temporaryQueue = jmsContext.createTemporaryQueue();
                message.setJMSReplyTo(temporaryQueue);
                producer.send(queue,message);
                String messageId = message.getJMSMessageID();
                System.out.println("Message Id while sending the message :"+messageId);

                JMSConsumer consumer = jmsContext.createConsumer(queue);
                Message receivedMessage =  consumer.receive();
                System.out.println(receivedMessage.getBody(String.class));
                String receivedMessageId = receivedMessage.getJMSMessageID();
                System.out.println("Received Message ID :"+receivedMessageId);
                Queue replyQueue = (Queue) receivedMessage.getJMSReplyTo();
                JMSProducer replyProducer = jmsContext.createProducer();
                TextMessage replyTextMessage = jmsContext.createTextMessage("I am waiting");
                replyTextMessage.setJMSCorrelationID(receivedMessageId);
                replyProducer.send(replyQueue,replyTextMessage);

                JMSConsumer consumer1 = jmsContext.createConsumer(temporaryQueue);
                Message receivedReplyMessage = consumer1.receive();
                System.out.println(receivedReplyMessage.getBody(String.class));
                System.out.println("ReceivedReplyCorrelationId :"+receivedReplyMessage.getJMSCorrelationID());

            }
        } catch (NamingException | JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
