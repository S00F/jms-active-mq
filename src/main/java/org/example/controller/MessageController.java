package org.example.controller;

import jakarta.validation.Valid;
import org.example.consumer.MessageConsumer;
import org.example.model.MessageModel;
import org.example.producer.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private MessageConsumer messageConsumer;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@Valid @RequestBody MessageModel messageModel) {
        logger.info("Received message to send to {}: {}", messageModel.getDestination(), messageModel.getContent());
        messageProducer.send(messageModel.getDestination(), messageModel.getContent());
        return ResponseEntity.ok("Message sent: " + messageModel.getContent());
    }

    @GetMapping("/receive")
    public ResponseEntity<String> receiveMessage(@RequestParam String destination) {
        logger.info("Receive request for destination: {}", destination);
        String message = messageConsumer.receive(destination);
        if (message != null) {
            return ResponseEntity.ok("Message received: " + message);
        } else {
            return ResponseEntity.ok("No message found.");
        }
    }
}
