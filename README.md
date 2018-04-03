# N26 code challenge

# Transaction and Statistics API

## Prerequisites

knowledge of Java 1.8,H2 In-memory DB and SpringBoot is required. 


### Overview

Exposing API for saving Transaction  and statistics view.<br />
Used Java 1.8 with spring boot in combination with H2 In-memory DB for data storing.<br />
Data Base state can be viewed using below URL
http://localhost:8080/h2-console
username=sa
password=sa

####API URLS
POST http://localhost:8080/transactions<br />
GET http://localhost:8080/statistics
 
## Getting up and running locally

### System requirements

* Maven 3.x
* Java 1.8


### Steps

#### To Build

```
cd ~/git/n26_code_challenge
mvn clean install
```

#### To Run

```
cd ~/git/n26_code_challenge
java -Dspring.profiles.active=local -jar target/n26_code_challenge-0.0.1-SNAPSHOT.jar
```

