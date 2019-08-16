package com.zhaidarbek.learn.accman.service;

import com.zhaidarbek.learn.accman.model.Account;
import com.zhaidarbek.learn.accman.service.exception.MoneyTransferFailedException;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account createAccount(Account account);

    Account getAccountById(Long accountId);

    List<Account> listAccounts();

    void transferFunds(Long accountFromId, Long accountToId, BigDecimal amount) throws MoneyTransferFailedException;
}
