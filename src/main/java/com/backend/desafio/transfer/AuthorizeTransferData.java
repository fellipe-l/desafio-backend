package com.backend.desafio.transfer;

public class AuthorizeTransferData {
    private Boolean authorization;

    public AuthorizeTransferData() {}

    public AuthorizeTransferData(Boolean authorization) {
        this.authorization = authorization;
    }

    public Boolean getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Boolean authorization) {
        this.authorization = authorization;
    }

    @Override
    public String toString() {
        return "AuthorizeTransferData{" +
                "authorization=" + authorization +
                '}';
    }
}
