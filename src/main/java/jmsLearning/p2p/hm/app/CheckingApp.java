package jmsLearning.p2p.hm.app;

import jmsLearning.p2p.hm.model.Passenger;

import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CheckingApp {
    public static void main(String args[]) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/requestQueue");

        Passenger passenger = new Passenger();
    }
}
