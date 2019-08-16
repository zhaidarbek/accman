package com.zhaidarbek.learn.accman.dao;

import com.zhaidarbek.learn.accman.model.Account;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;
import java.util.Optional;

public interface AccountDao {

    @SqlQuery("select * from account")
    @RegisterRowMapper(AccountRowMapper.class)
    List<Account> listAccounts();

    @SqlQuery("select * from account where id=:id")
    @RegisterRowMapper(AccountRowMapper.class)
    Optional<Account> getAccountById(@Bind("id") Long accountFromId);

    @SqlUpdate("insert into account(id, name, balance) values(:id, :name, :balance)")
    @GetGeneratedKeys("id")
    Long createAccount(@BindBean Account account);

    @SqlUpdate("update account set balance=:balance where id=:id")
    void updateAccount(@BindBean Account account);

    @Transaction
    default void updateAccountsInTransaction(Account... accounts) {
        for (Account account : accounts) {
            updateAccount(account);
        }
    }
}
