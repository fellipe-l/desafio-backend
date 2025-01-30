package com.backend.desafio.transfer;

import java.math.BigDecimal;

public class Transfer {
    private BigDecimal value;
    private Long payerId;
    private Long payeeId;

    public Transfer() {}

    public Transfer(BigDecimal value, Long payerId, Long payeeId) {
        this.value = value;
        this.payerId = payerId;
        this.payeeId = payeeId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "value=" + value +
                ", payerId=" + payerId +
                ", payeeId=" + payeeId +
                '}';
    }
}
