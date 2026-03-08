package org.example.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    public String receive(String destination) {
        try {
            logger.info("Receiving message from {}", destination);
            String message = (String) jmsTemplate.receiveAndConvert(destination);
            if (message != null) {
                logger.info("Message received from {}: {}", destination, message);
            } else {
                logger.info("No message found in {}", destination);
            }
            return message;
        } catch (Exception e) {
            logger.error("Failed to receive message from {}: {}", destination, e.getMessage(), e);
            throw new RuntimeException("Failed to receive message from " + destination, e);
        }
    }
}
