package com.bank.clients.client.enums;

public enum MessageOperations {
    SUCCESS("success"),
    NOT_FOUND("notfound"),;

    private String description;

    MessageOperations(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
