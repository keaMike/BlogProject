package com.blog.project.Model;

import java.util.Date;

public class Message {

    private int messageid;
    private int receiverId;
    private int senderId;
    private String message;
    private Date date;

    public Message() {
    }

    public Message(int messageid, int receiverId, int senderId, String message, Date date) {
        this.messageid = messageid;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.message = message;
        this.date = date;
    }

    public int getMessageid() {
        return messageid;
    }

    public void setMessageid(int messageid) {
        this.messageid = messageid;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "receiverId=" + receiverId +
                ", senderId=" + senderId +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
