package com.example.chatapp_120170503.models;

public class Message {
    private String text;
    private String senderId;
    private String receiverId;
    private String senderName;
    private String receiverName;

    public Message() {
    }

    public Message(String text, String senderId, String receiverId, String senderName, String receiverName) {
        this.text = text;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.receiverName = receiverName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
