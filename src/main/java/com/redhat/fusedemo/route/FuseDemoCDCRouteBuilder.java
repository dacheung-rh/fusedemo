package com.redhat.fusedemo.route;

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

        from("direct:logger-cdc")
        .log("CDC 01 data transferred");

        // table 2
        from("kafka:{{kafka.topic.cdc-table02}}?groupId={{kafka.topic.cdcgroup.id}}")
        .routeId("kafka-consumer-cdc-route-02")
        .log("History Received 02: ${body}")
        .to("mongodb:mongoBean?database={{spring.data.mongodb.database}}&collection=history.{{kafka.topic.cdc-table02}}&operation=insert")
        .to("direct:logger-cdc-02");

        from("direct:logger-cdc-02")
        .log("CDC 02 data transferred");

        // table 3
        // from("kafka:{{kafka.topic.cdc-table03}}?groupId={{kafka.topic.cdcgroup.id}}")
        // .routeId("kafka-consumer-cdc-route-03")
        // .log("History Received 03: ${body}")
        // .to("mongodb:mongoBean?database={{spring.data.mongodb.database}}&collection=history.{{kafka.topic.cdc-table03}}&operation=insert")
        // .to("direct:logger-cdc-03");

        // from("direct:logger-cdc-03")
        // .log("CDC 03 data transferred");

        // table 4
        // from("kafka:{{kafka.topic.cdc-table04}}?groupId={{kafka.topic.cdcgroup.id}}")
        // .routeId("kafka-consumer-cdc-route-04")
        // .log("History Received 04: ${body}")
        // .to("mongodb:mongoBean?database={{spring.data.mongodb.database}}&collection=history.{{kafka.topic.cdc-table04}}&operation=insert")
        // .to("direct:logger-cdc-04");

        // from("direct:logger-cdc-04")
        // .log("CDC 04 data transferred");
    }
}
