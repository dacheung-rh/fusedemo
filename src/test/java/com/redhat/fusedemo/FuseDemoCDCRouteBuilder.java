package com.redhat.fusedemo;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FuseDemoCDCRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        onException(Exception.class).setBody(constant("errorOccured")).maximumRedeliveries(0).continued(true);

        // Consume CDC data history from Kafka topic and write it to the NoSQL database
        // table 1
        from("kafka:{{kafka.topic.cdc-table01}}?groupId={{kafka.topic.cdcgroup.id}}")
        .routeId("kafka-consumer-cdc-route")
        .log("History Received: ${body}")
        .to("mongodb:mongoBean?database={{spring.data.mongodb.database}}&collection=history.{{kafka.topic.cdc-table01}}&operation=insert")
        .to("direct:logger-cdc");
    }
}
