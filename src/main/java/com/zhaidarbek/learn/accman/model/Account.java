package com.zhaidarbek.learn.accman.model;

import java.math.BigDecimal;

public class Account {
    private final Long id;
    private final String name;
    private final BigDecimal balance;

    public Account(String name, BigDecimal balance) {
        this(null, name, balance);
    }

    public Account(Long id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Account updateBalance(BigDecimal newBalance) {
        return new Account(this.getId(), this.getName(), newBalance);
    }
}
