package com.zhaidarbek.learn.accman.dao;

import com.zhaidarbek.learn.accman.model.Account;
import org.jdbi.v3.testing.JdbiRule;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class AccountDaoImplTest {

    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE account (id INTEGER PRIMARY KEY, name VARCHAR2(255) NOT NULL, balance DECIMAL(20, 2) NOT NULL)";
    private static final String CLEANUP_TABLE_ACCOUNT = "DELETE FROM account";
    private static final String INSERT_INTO_ACCOUNT = "INSERT INTO account(id, name, balance) VALUES(?, ?, ?)";

    @ClassRule
    public static JdbiRule jdbiRule = JdbiRule.h2();

    private AccountDaoImpl accountDao;

    @BeforeClass
    public static void setupDatabase() {
        jdbiRule.getJdbi().withHandle(handle -> handle.execute(CREATE_TABLE_ACCOUNT));
    }

    @Before
    public void setup() {
        accountDao = new AccountDaoImpl(jdbiRule.getJdbi());
        jdbiRule.getJdbi().withHandle(handle -> {
            handle.execute(INSERT_INTO_ACCOUNT, 1L, "Savings Account", 100);
            handle.execute(INSERT_INTO_ACCOUNT, 2L, "Checking Account", 200);
            return handle;
        });
    }

    @After
    public void cleanup() {
        jdbiRule.getJdbi().withHandle(handle -> handle.execute(CLEANUP_TABLE_ACCOUNT));
    }

    @Test
    public void getAccountById() {
        Optional<Account> accountById = accountDao.getAccountById(1L);
        assertTrue(accountById.isPresent());
        assertThat(accountById.get().getBalance().intValue(), equalTo(100));

        accountById = accountDao.getAccountById(2L);
        assertTrue(accountById.isPresent());
        assertThat(accountById.get().getBalance().intValue(), equalTo(200));
    }

    @Test
    public void updateAccountsInTransaction() {
        Account account1 = new Account(1L, "Savings Account", BigDecimal.ZERO);
        Account account2 = new Account(2L, "Checking Account", BigDecimal.TEN);

        accountDao.updateAccountsInTransaction(account1, account2);

        jdbiRule.getJdbi().withHandle(handle -> {
            BigDecimal balance1 = handle.createQuery("select balance from account where id=1").mapTo(BigDecimal.class).findOnly();
            BigDecimal balance2 = handle.createQuery("select balance from account where id=2").mapTo(BigDecimal.class).findOnly();
            assertThat(balance1.intValue(), equalTo(0));
            assertThat(balance2.intValue(), equalTo(10));
            return handle;
        });
    }
}