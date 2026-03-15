package org.example.consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.support.converter.MessageConversionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageConsumerTest {

    @Mock
    private JmsOperations jmsTemplate;

    @InjectMocks
    private MessageConsumer messageConsumer;

    @Test
    void receive_returnsMessage_whenMessageAvailable() {
        when(jmsTemplate.receiveAndConvert("TEST.QUEUE")).thenReturn("hello");

        String result = messageConsumer.receive("TEST.QUEUE");

        assertEquals("hello", result);
    }

    @Test
    void receive_returnsNull_whenNoMessageAvailable() {
        when(jmsTemplate.receiveAndConvert("TEST.QUEUE")).thenReturn(null);

        String result = messageConsumer.receive("TEST.QUEUE");

        assertNull(result);
    }

    @Test
    void receive_rethrowsJmsException() {
        JmsException jmsException = new MessageConversionException("conversion failed");
        when(jmsTemplate.receiveAndConvert("TEST.QUEUE")).thenThrow(jmsException);

        assertThrows(JmsException.class, () -> messageConsumer.receive("TEST.QUEUE"));
    }

    @Test
    void receive_wrapsGenericExceptionInJmsException() {
        when(jmsTemplate.receiveAndConvert("TEST.QUEUE")).thenThrow(new RuntimeException("unexpected"));

        assertThrows(JmsException.class, () -> messageConsumer.receive("TEST.QUEUE"));
    }
}