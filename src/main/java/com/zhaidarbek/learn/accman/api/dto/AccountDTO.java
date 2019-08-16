package com.zhaidarbek.learn.accman.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountDTO {
    private Long id;
    private String name;
    private String balance;

    @JsonProperty
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
