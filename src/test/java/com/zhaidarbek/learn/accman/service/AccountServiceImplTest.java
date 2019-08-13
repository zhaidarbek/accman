package com.zhaidarbek.learn.accman.service;

import com.zhaidarbek.learn.accman.dao.AccountDao;
import com.zhaidarbek.learn.accman.model.Account;
import com.zhaidarbek.learn.accman.service.exception.AccountNotFoundException;
import com.zhaidarbek.learn.accman.service.exception.MoneyTransferFailedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Captor
    ArgumentCaptor<Account> accountFromCaptor;
    @Captor
    ArgumentCaptor<Account> accountToCaptor;
    @Mock
    private AccountDao accountDao;
    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    public void transferFunds() throws MoneyTransferFailedException {
        Account accountFrom = new Account(1L, "test account", BigDecimal.TEN);
        Account accountTo = new Account(2L, "test account 2", BigDecimal.TEN);
        when(accountDao.getAccountById(accountFrom.getId())).thenReturn(Optional.of(accountFrom));
        when(accountDao.getAccountById(accountTo.getId())).thenReturn(Optional.of(accountTo));

        accountService.transferFunds(accountFrom.getId(), accountTo.getId(), BigDecimal.valueOf(10L));

        verify(accountDao).updateAccountsInTransaction(accountFromCaptor.capture(), accountToCaptor.capture());
        assertThat(accountFromCaptor.getValue().getBalance().intValue(), equalTo(0));
        assertThat(accountToCaptor.getValue().getBalance().intValue(), equalTo(20));
    }

    @Test
    public void transferFundsFailsWhenNegativeAmount() throws MoneyTransferFailedException {
        exception.expect(MoneyTransferFailedException.class);
        exception.expectMessage("Invalid transfer amount [-1.00]");
        accountService.transferFunds(1L, 2L, BigDecimal.valueOf(-1L));
    }

    @Test
    public void transferFundsFailsWhenZeroAmount() throws MoneyTransferFailedException {
        exception.expect(MoneyTransferFailedException.class);
        exception.expectMessage("Invalid transfer amount [0.00]");
        accountService.transferFunds(1L, 2L, BigDecimal.ZERO);
    }

    @Test
    public void transferFundsFailsWhenAccountNotFound() throws MoneyTransferFailedException {
        exception.expect(AccountNotFoundException.class);
        accountService.transferFunds(1L, 2L, BigDecimal.TEN);
    }

    @Test
    public void transferFundsFailsWhenInsufficientBalance() throws MoneyTransferFailedException {
        Account accountFrom = new Account(1L, "test account", BigDecimal.TEN);
        Account accountTo = new Account(2L, "test account 2", BigDecimal.TEN);
        when(accountDao.getAccountById(accountFrom.getId())).thenReturn(Optional.of(accountFrom));
        when(accountDao.getAccountById(accountTo.getId())).thenReturn(Optional.of(accountTo));

        exception.expect(MoneyTransferFailedException.class);
        exception.expectMessage("Insufficient funds in account 'test account'");

        accountService.transferFunds(accountFrom.getId(), accountTo.getId(), BigDecimal.valueOf(11L));
    }

}