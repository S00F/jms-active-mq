package org.example.consumer;


import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@EnableJms
public class MessageListener {

    @JmsListener(destination = "TEST.QUEUE")
    public void receive(String message) {
        System.out.println("Message received: " + message);
    }
}