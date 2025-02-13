package com.bank.clients.client.enums;

public enum ClientStatus {
    ACTIVE("Activo"),
    INACTIVE("Inactivo"),
    PENDING("Pendiente"),
    BLOCKED("Bloqueado");

    private String description;

    ClientStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
