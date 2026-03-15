package org.example.producer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsOperations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageProducerTest {

    @Mock
    private JmsOperations jmsTemplate;

    @InjectMocks
    private MessageProducer messageProducer;

    @Test
    void send_success() {
        messageProducer.send("TEST.QUEUE", "hello");

        verify(jmsTemplate).convertAndSend("TEST.QUEUE", "hello");
    }

    @Test
    void send_throwsRuntimeException_whenJmsTemplateFails() {
        doThrow(new RuntimeException("broker down")).when(jmsTemplate).convertAndSend("TEST.QUEUE", "hello");

        assertThrows(RuntimeException.class, () -> messageProducer.send("TEST.QUEUE", "hello"));
    }
}