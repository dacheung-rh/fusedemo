package com.redhat.fusedemo;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
public class FuseDemoRouteBuilderTest {

	private final Logger LOGGER = LoggerFactory.getLogger(FuseDemoRouteBuilderTest.class);

	@Autowired
	private ConsumerTemplate consumerTemplate;

	@Autowired
	private CamelContext context;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void testKafkaConsumerCDCRoute() throws Exception {

		RouteDefinition kafkaRouteDef = context.getRouteDefinition("kafka-consumer-cdc-route");
		configureRoute(kafkaRouteDef, "logger-cdc");
		context.startRoute(kafkaRouteDef.getId());
		assertErrorNotOccured();
		context.stopRoute(kafkaRouteDef.getId());
	}

	@Test
	public void testKafkaConsumerCUDRoute() throws Exception {

		RouteDefinition kafkaRouteDef = context.getRouteDefinition("kafka-consumer-cud-route");
		configureRoute(kafkaRouteDef, "logger-cud");
		context.startRoute(kafkaRouteDef.getId());
		assertErrorNotOccured();
		context.stopRoute(kafkaRouteDef.getId());
	}

	private void configureRoute(RouteDefinition routeDef, String endpoint) throws Exception{
		routeDef.adviceWith(context, new AdviceWithRouteBuilder() {
				@Override
				public void configure() {
					interceptSendToEndpoint("direct:" + endpoint)
					.skipSendToOriginalEndpoint()
					.to("direct:output");
				}
			}
		);
	}

	private void assertErrorNotOccured() throws Exception{
		String body = consumerTemplate.receive("direct:output").getIn().getBody(String.class);
		assertNotEquals("errorOccured", body); 
	}

}
