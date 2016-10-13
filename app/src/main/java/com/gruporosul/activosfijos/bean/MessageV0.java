package com.gruporosul.activosfijos.bean;

/**
 * Created by Cristian Ramírez on 27/09/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class MessageV0 {
    private final String messageTitle;
    private final String messageContent;

    public MessageV0(String messageTitle, String messageContent) {
        this.messageTitle = messageTitle;
        this.messageContent = messageContent;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }
}
