# Account Manager 

Sample application showing how to use dropwizard and jdbi to build REST service. 

How to start the Account Manager  application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/accman-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:9000/accman/accounts`

To create accounts invoke following commands 
--
```
curl -H "Content-Type: application/json" -X POST -d '{"name":"Checking Account","balance":100.00}' http://localhost:9000/accman/accounts
curl -H "Content-Type: application/json" -X POST -d '{"name":"Saving Account","balance":100.00}' http://localhost:9000/accman/accounts
```

Use below command to transfer money from account with ID 1 to account with ID 2
---
```
curl -H "Content-Type: application/json" -X POST -d '{"accountFromId":1,"accountToId":2,"amount":"100.00"}' http://localhost:9000/accman/accounts/transfer
```

Now check the balance of both accounts
---
```
curl http://localhost:9000/accman/accounts
```

Health Check
---

To see your applications health enter url `http://localhost:9001/healthcheck`
