package com.zhaidarbek.learn.accman.api.resource;

import com.zhaidarbek.learn.accman.api.dto.AccountDTO;
import com.zhaidarbek.learn.accman.api.dto.TransferFundsDTO;
import com.zhaidarbek.learn.accman.model.Account;
import com.zhaidarbek.learn.accman.service.AccountService;
import com.zhaidarbek.learn.accman.service.exception.MoneyTransferFailedException;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;

@Path("/accounts/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountManagerResource {

    private final AccountService accountService;

    @Inject
    public AccountManagerResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @POST
    public Response createAccount(AccountDTO account, @Context UriInfo uriInfo) {
        Account newAccount = new Account(account.getName(), new BigDecimal(account.getBalance()));
        Account savedAccount = accountService.createAccount(newAccount);
        URI resourceUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(savedAccount.getId())).build();
        return Response.created(resourceUri).entity(savedAccount).build();
    }

    @GET
    public Response listAccounts() {
        //todo use mapping framework like orika
        return Response.ok(accountService.listAccounts()).build();
    }

    @GET
    @Path("/{accountId}")
    public Response getAccount(@PathParam("accountId") Long accountId) {
        return Response.ok(accountService.getAccountById(accountId)).build();
    }

    @POST
    @Path("/transfer")
    public Response transferFunds(@NotNull @Valid TransferFundsDTO transferFundsDTO) throws MoneyTransferFailedException {
        // TODO use converter/validator and mapper handler and mapper
        accountService.transferFunds(transferFundsDTO.getAccountFromId(), transferFundsDTO.getAccountToId(), new BigDecimal(transferFundsDTO.getAmount()));
        return Response.ok().build();
    }
}
