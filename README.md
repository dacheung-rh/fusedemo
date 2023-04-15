# CDC Fuse Demo

This fuse demo application developed using camel springboot.  It consumed CDC kafka topics and update the change history to MongoDB.

## build with testKafkaConsumerCDCRoute test
------------------------------------------------
Insert cdc history to MongoDB collection
```
$ ./mvnw -Dtest=FuseDemoRouteBuilderTest#testKafkaConsumerCDCRoute clean test
```


## build with testKafkaConsumerCUDRoute test
------------------------------------------------
Insert / Update / Delete of CDC to MongoDB collection
```
$ ./mvnw -Dtest=FuseDemoRouteBuilderTest#testKafkaConsumerCDCRoute clean test
```

## Package FuseDemoRouteBuilder
------------------------------------------------
```
$ ./mvnw clean package -DskipTests=true
```

## Run Fuse Springboot app
------------------------------------------------
```
$ java -jar target/fuse-demo-service-0.0.1-SNAPSHOT.jar
```
