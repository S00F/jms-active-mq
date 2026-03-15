package org.example.model;

import jakarta.validation.constraints.NotBlank;

public class MessageModel {
    @NotBlank(message = "Content is required")
    private String content;

    @NotBlank(message = "Destination is required")
    private String destination;

    public MessageModel() {}

    public MessageModel(String content, String destination) {
        this.content = content;
        this.destination = destination;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
}