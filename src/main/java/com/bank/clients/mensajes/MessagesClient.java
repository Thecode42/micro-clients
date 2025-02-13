package com.bank.clients.mensajes;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("bank.message.client")
public class MessagesClient {
    private String success;
    private String update;
    private String delete;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }
}
