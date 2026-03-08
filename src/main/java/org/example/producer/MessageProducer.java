package org.example.producer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(String destination, String message) {
        try {
            logger.info("Sending message to {}: {}", destination, message);
            jmsTemplate.convertAndSend(destination, message);
            logger.info("Message sent successfully to {}", destination);
        } catch (Exception e) {
            logger.error("Failed to send message to {}: {}", destination, e.getMessage(), e);
            throw new RuntimeException("Failed to send message to " + destination, e);
        }
    }
}
