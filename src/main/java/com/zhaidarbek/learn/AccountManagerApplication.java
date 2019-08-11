package com.zhaidarbek.learn;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AccountManagerApplication extends Application<AccountManagerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new AccountManagerApplication().run(args);
    }

    @Override
    public String getName() {
        return "Account Manager ";
    }

    @Override
    public void initialize(final Bootstrap<AccountManagerConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final AccountManagerConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
