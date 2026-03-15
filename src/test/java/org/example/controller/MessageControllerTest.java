package org.example.controller;

import org.example.consumer.MessageConsumer;
import org.example.exception.GlobalExceptionHandler;
import org.example.producer.MessageProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private MessageProducer messageProducer;

    @Mock
    private MessageConsumer messageConsumer;

    @InjectMocks
    private MessageController messageController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void sendMessage_success() throws Exception {
        mockMvc.perform(post("/messages/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"destination\":\"TEST.QUEUE\",\"content\":\"hello\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Message sent: hello"));

        verify(messageProducer).send("TEST.QUEUE", "hello");
    }

    @Test
    void sendMessage_missingContent_returns400() throws Exception {
        mockMvc.perform(post("/messages/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"destination\":\"TEST.QUEUE\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendMessage_missingDestination_returns400() throws Exception {
        mockMvc.perform(post("/messages/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"hello\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendMessage_blankContent_returns400() throws Exception {
        mockMvc.perform(post("/messages/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"destination\":\"TEST.QUEUE\",\"content\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void receiveMessage_returnsMessage_whenAvailable() throws Exception {
        when(messageConsumer.receive("TEST.QUEUE")).thenReturn("hello");

        mockMvc.perform(get("/messages/receive").param("destination", "TEST.QUEUE"))
                .andExpect(status().isOk())
                .andExpect(content().string("Message received: hello"));
    }

    @Test
    void receiveMessage_returnsNoMessageFound_whenNull() throws Exception {
        when(messageConsumer.receive("TEST.QUEUE")).thenReturn(null);

        mockMvc.perform(get("/messages/receive").param("destination", "TEST.QUEUE"))
                .andExpect(status().isOk())
                .andExpect(content().string("No message found."));
    }

    @Test
    void receiveMessage_missingDestination_returns400() throws Exception {
        mockMvc.perform(get("/messages/receive"))
                .andExpect(status().isBadRequest());
    }
}