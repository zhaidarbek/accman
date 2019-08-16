package com.zhaidarbek.learn.accman.api.mapper;

import com.zhaidarbek.learn.accman.service.exception.MoneyTransferFailedException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class MoneyTransferFailedExceptionMapper implements ExceptionMapper<MoneyTransferFailedException> {
    @Override
    public Response toResponse(MoneyTransferFailedException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();

    }
}
