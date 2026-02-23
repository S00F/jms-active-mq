package org.example.model;

public class MessageModel {
    private String content;
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