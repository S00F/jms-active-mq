package org.example.controller;

import org.example.model.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessageModel messageModel) {
        jmsTemplate.convertAndSend(messageModel.getDestination(), messageModel.getContent());
        return ResponseEntity.ok("Message sent: " + messageModel.getContent());
    }


    @GetMapping("/receive")
    public ResponseEntity<String> receiveMessage(@RequestParam String destination) {
        String message = (String) jmsTemplate.receiveAndConvert(destination);
        if (message != null) {
            return ResponseEntity.ok("Message received: " + message);
        } else {
            return ResponseEntity.ok("No message found.");
        }
    }
}
