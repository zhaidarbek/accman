package com.zhaidarbek.learn.accman.service;

import com.zhaidarbek.learn.accman.service.exception.MoneyTransferFailedException;

import java.math.BigDecimal;

public interface AccountService {
    void transferFunds(Long accountFromId, Long accountToId, BigDecimal amount) throws MoneyTransferFailedException;
}
