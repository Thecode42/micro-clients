package com.bank.clients.mensajes;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("bank.message.exceptions.client")
public class MessagesException {
    private String notfound;

    public String getNotfound() {
        return notfound;
    }

    public void setNotfound(String notfound) {
        this.notfound = notfound;
    }
}
