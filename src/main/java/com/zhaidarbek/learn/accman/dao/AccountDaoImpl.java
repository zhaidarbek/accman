package com.zhaidarbek.learn.accman.dao;

import com.zhaidarbek.learn.accman.model.Account;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.StatementContext;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AccountDaoImpl implements AccountDao {

    public static final String SQL_GET_BY_ID = "select * from account where id=?";
    public static final String SQL_UPDATE_BY_ID = "update account set balance=? where id=?";
    private final Jdbi jdbi;

    @Inject
    public AccountDaoImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Optional<Account> getAccountById(Long accountFromId) {
        return jdbi.withHandle(handle -> {
            Query query = handle.createQuery(SQL_GET_BY_ID).bind(0, accountFromId);
            return query.map(this::parseResultSet).findFirst();
        });
    }

    private Account parseResultSet(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Account(rs.getLong("id"),
                rs.getString("name"),
                rs.getBigDecimal("balance"));
    }

    @Override
    public void updateAccountsInTransaction(Account... accounts) {
        jdbi.inTransaction(handle -> {
            for (Account account : accounts) {
                handle.execute(SQL_UPDATE_BY_ID, account.getBalance(), account.getId());
            }
            return handle;
        });
    }
}
