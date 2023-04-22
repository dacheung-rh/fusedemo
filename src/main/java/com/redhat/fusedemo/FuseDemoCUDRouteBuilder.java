package com.redhat.fusedemo;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FuseDemoCUDRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        onException(Exception.class).setBody(constant("errorOccured")).maximumRedeliveries(0).continued(true);

        // transform insert / update / delete actions the NoSQL database
        from("kafka:{{kafka.topic.cdc-table01}}?groupId={{kafka.topic.cudgroup.id}}")
        .routeId("kafka-consumer-cud-route")
        .choice()
            .when().jsonpath("$.payload[?(@.op == 'c')]").setBody().jsonpathWriteAsString("$.payload.after").log("Insert action ${body}")
                .to("mongodb:mongoBean?database={{spring.data.mongodb.database}}&collection={{kafka.topic.cdc-table01}}&operation=insert")
                .to("direct:logger-cud")
            .when().jsonpath("$.payload[?(@.op == 'u')]").setBody().jsonpathWriteAsString("$.payload.after").log("Update action ${body}")
                .to("mongodb:mongoBean?database={{spring.data.mongodb.database}}&collection={{kafka.topic.cdc-table01}}&operation=update")
                .to("direct:logger-cud")
            .when().jsonpath("$.payload[?(@.op == 'd')]").setBody().jsonpathWriteAsString("$.payload.before").log("Delete action ${body}")
                .to("mongodb:mongoBean?database={{spring.data.mongodb.database}}&collection={{kafka.topic.cdc-table01}}&operation=remove")
                .to("direct:logger-cud")
        .otherwise()
            .log("Received ${body}")
            .to("direct:logger-cud")
        .end();
  
        from("direct:logger-cud")
        .log("CUD data transferred");
    }
}
