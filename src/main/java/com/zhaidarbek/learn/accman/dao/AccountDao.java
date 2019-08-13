package com.zhaidarbek.learn.accman.dao;

import com.zhaidarbek.learn.accman.model.Account;

import java.util.Optional;

public interface AccountDao {
    Optional<Account> getAccountById(Long accountFromId);

    void updateAccountsInTransaction(Account... accounts);
}
