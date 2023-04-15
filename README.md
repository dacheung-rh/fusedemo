# CDC Fuse Demo

This fuse demo application developed using camel springboot.  It consumed CDC kafka topics and update the change history to MongoDB.

./images/cdc_amq_fuse_demo.png


## Project structure
```
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── redhat
    │   │           └── fusedemo
    │   │               ├── Application.java
    │   │               ├── config
    │   │               │   ├── MongoConfig.java
    │   │               │   └── MongoProperties.java
    │   │               └── route
    │   │                   ├── FuseDemoCDCRouteBuilder.java
    │   │                   └── FuseDemoCUDRouteBuilder.java
    │   └── resources
    │       └── application.properties
    └── test
        └── java
            └── com
                └── redhat
                    └── fusedemo
                        └── FuseDemoRouteBuilderTest.java
```

## Prerequisites
------------------------------------------------
1) MySQL server available for CDC target
2) Kafka server with MySQL debezium connector configured
3) MongoDB available to update target
4) Update the src/main/resources/application.properties file
   - database servers and credentials
   - kafka topics

## build with testKafkaConsumerCDCRoute test
------------------------------------------------
Insert cdc history to MongoDB collection
```
./mvnw -Dtest=FuseDemoRouteBuilderTest#testKafkaConsumerCDCRoute clean test
```


## build with testKafkaConsumerCUDRoute test
------------------------------------------------
Insert / Update / Delete of CDC to MongoDB collection
```
./mvnw -Dtest=FuseDemoRouteBuilderTest#testKafkaConsumerCDCRoute clean test
```

## Package FuseDemoRouteBuilder
------------------------------------------------
```
./mvnw clean package -DskipTests=true
```

## Run Fuse Springboot app
------------------------------------------------
```
java -jar target/fuse-demo-service-0.0.1-SNAPSHOT.jar
```
