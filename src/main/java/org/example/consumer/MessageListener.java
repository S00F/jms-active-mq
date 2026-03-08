package org.example.consumer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    @JmsListener(destination = "TEST.QUEUE")
    public void receive(String message) {
        try {
            logger.info("Message received from TEST.QUEUE: {}", message);
            processMessage(message);
            logger.info("Message processed successfully");
        } catch (Exception e) {
            logger.error("Failed to process message: {}", e.getMessage(), e);
        }
    }

    private void processMessage(String message) {
        logger.debug("Processing message: {}", message);
    }
}