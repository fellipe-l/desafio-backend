package com.backend.desafio.transfer;

public class AuthorizeTransfer {
    private String status;
    private AuthorizeTransferData data;

    public AuthorizeTransfer() {}

    public AuthorizeTransfer(String status, AuthorizeTransferData data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AuthorizeTransferData getData() {
        return data;
    }

    public void setData(AuthorizeTransferData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AuthorizeTransfer{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
