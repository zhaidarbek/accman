package com.zhaidarbek.learn.accman.service;

import com.zhaidarbek.learn.accman.dao.AccountDao;
import com.zhaidarbek.learn.accman.model.Account;
import com.zhaidarbek.learn.accman.service.exception.AccountNotFoundException;
import com.zhaidarbek.learn.accman.service.exception.MoneyTransferFailedException;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class AccountServiceImpl implements AccountService {
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    private final AccountDao accountDao;

    @Inject
    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Account createAccount(Account account) {
        Long accountId = accountDao.createAccount(account);
        return getAccountById(accountId);
    }

    @Override
    public Account getAccountById(Long accountId) {
        return accountDao.getAccountById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    @Override
    public List<Account> listAccounts() {
        return accountDao.listAccounts();
    }

    @Override
    public void transferFunds(Long accountFromId, Long accountToId, BigDecimal amount) throws MoneyTransferFailedException {
        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            throw new MoneyTransferFailedException(String.format("Invalid transfer amount [%s]", DF.format(amount)));
        }

        Account accountFrom = accountDao.getAccountById(accountFromId).orElseThrow(() -> new AccountNotFoundException(accountFromId));
        Account accountTo = accountDao.getAccountById(accountToId).orElseThrow(() -> new AccountNotFoundException(accountToId));
        if (accountFrom.getBalance().compareTo(amount) < 0) {
            throw new MoneyTransferFailedException(String.format("Insufficient funds in account '%s'", accountFrom.getName()));
        }

        accountFrom = accountFrom.updateBalance(accountFrom.getBalance().subtract(amount));
        accountTo = accountTo.updateBalance(accountTo.getBalance().add(amount));

        accountDao.updateAccountsInTransaction(accountFrom, accountTo);
    }
}
