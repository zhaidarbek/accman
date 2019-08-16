package com.zhaidarbek.learn.accman.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferFundsDTO {
    private final Long accountFromId;
    private final Long accountToId;
    private final String amount;


    @JsonCreator
    public TransferFundsDTO(@JsonProperty("accountFromId") Long accountFromId,
                            @JsonProperty("accountToId") Long accountToId,
                            @JsonProperty("amount") String amount) {
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.amount = amount;
    }

    public Long getAccountFromId() {
        return accountFromId;
    }

    public Long getAccountToId() {
        return accountToId;
    }

    public String getAmount() {
        return amount;
    }
}
