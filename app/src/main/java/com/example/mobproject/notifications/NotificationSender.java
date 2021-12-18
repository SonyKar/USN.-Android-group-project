package com.example.mobproject.notifications;

public class NotificationSender {
    public Data data;
    public String toUserToken;

    public NotificationSender(Data data, String toUserToken) {
        this.data = data;
        this.toUserToken = toUserToken;
    }

    public NotificationSender(){}
}
