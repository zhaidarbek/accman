package com.zhaidarbek.learn.accman.dao;

import com.zhaidarbek.learn.accman.model.Account;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRowMapper implements RowMapper<Account> {

    @Override
    public Account map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Account(rs.getLong("id"),
                rs.getString("name"),
                rs.getBigDecimal("balance"));
    }
}
