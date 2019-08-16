package com.zhaidarbek.learn.accman.service.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long accountId) {
        super(String.format("Account with ID [%s] doesn't exist", accountId));
    }
}
