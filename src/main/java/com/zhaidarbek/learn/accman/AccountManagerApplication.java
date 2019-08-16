package com.zhaidarbek.learn.accman;

import com.codahale.metrics.MetricRegistry;
import com.zhaidarbek.learn.accman.api.mapper.AccountNotFoundExceptionMapper;
import com.zhaidarbek.learn.accman.api.mapper.MoneyTransferFailedExceptionMapper;
import com.zhaidarbek.learn.accman.api.resource.AccountManagerResource;
import com.zhaidarbek.learn.accman.dao.AccountDao;
import com.zhaidarbek.learn.accman.service.AccountService;
import com.zhaidarbek.learn.accman.service.AccountServiceImpl;
import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.h2.tools.Server;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Validator;
import java.sql.SQLException;

public class AccountManagerApplication extends Application<AccountManagerConfiguration> {
    public static final String HEALTH_CHECK_NAME_FOR_DB = "relationalDatabase";
    private static final Logger LOG = LoggerFactory.getLogger(AccountManagerApplication.class);

    public static void main(final String[] args) throws Exception {
        new AccountManagerApplication().run(args);
    }

    @Override
    public String getName() {
        return "Account Manager ";
    }

    @Override
    public void initialize(final Bootstrap<AccountManagerConfiguration> bootstrap) {
        try {
            Server webServer = Server.createWebServer().start();
            LOG.info("Started web console for H2 database at {}", webServer.getURL());
        } catch (SQLException e) {
            LOG.warn("Failed to start web console for H2 database", e);
        }
    }

    @Override
    public void run(final AccountManagerConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(new Hk2InjectionBinder(configuration, environment));
        environment.jersey().register(AccountManagerResource.class);
        environment.jersey().register(MoneyTransferFailedExceptionMapper.class);
        environment.jersey().register(AccountNotFoundExceptionMapper.class);
    }

    private static class Hk2InjectionBinder extends AbstractBinder {

        private final AccountManagerConfiguration configuration;
        private final Environment environment;

        public Hk2InjectionBinder(final AccountManagerConfiguration configuration,
                                  final Environment environment) {
            this.configuration = configuration;
            this.environment = environment;
        }

        @Override
        protected void configure() {
            bind(configuration).to(AccountManagerConfiguration.class);
            bind(environment).to(Environment.class);
            bind(environment.lifecycle()).to(LifecycleEnvironment.class);
            bind(environment.metrics()).to(MetricRegistry.class);
            bind(environment.getValidator()).to(Validator.class);

            final Jdbi jdbi = new JdbiFactory().build(environment, configuration.getDataSourceFactory(), HEALTH_CHECK_NAME_FOR_DB);
            bind(jdbi).to(Jdbi.class);

            AccountDao accountDao = jdbi.onDemand(AccountDao.class);
            bind(accountDao).to(AccountDao.class);

            bind(AccountServiceImpl.class).to(AccountService.class);
        }
    }
}
