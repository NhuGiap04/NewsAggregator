# To run Searching API

This mini-project will set up an API on port 9696 by default to perform
searching function. This includes querying a MongoDB collection and sending
request to other functional API, so check your Internet connection!

To run, open Terminal in this folder. Make sure to have Maven and JDK >=17
installed, as well as system environment variables set.

Install dependencies
```commandline
mvn clean install
```
Run the API server
```commandline
mvn spring-boot:run
```

## Common problems

mvn not found? Install Maven AND set environment variables, tons of guides
on the Internet lol git gut.

Fatal error compiling invalid flag: --release? Most likely your Java environment
variable points to JDK 1.8 or older. I can't find a way to make it work for JDK
1.8 yet. Just install JDK 17 or later.
