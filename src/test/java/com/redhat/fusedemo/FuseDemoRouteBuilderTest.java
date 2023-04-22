package com.redhat.fusedemo;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest
public class FuseDemoRouteBuilderTest {

	private final Logger LOGGER = LoggerFactory.getLogger(FuseDemoRouteBuilderTest.class);

	@Autowired
	private ConsumerTemplate consumerTemplate;

	@Autowired
	private ModelCamelContext context;

	@Test
	public void testKafkaConsumerCDCRoute() throws Exception {

		context.start();
		RouteDefinition kafkaRouteDef = context.getRouteDefinition("kafka-consumer-cdc-route");
		configureRoute(kafkaRouteDef, "logger-cdc");
		context.getRouteController().startRoute(kafkaRouteDef.getId());
		// assertErrorNotOccured();
		context.getRouteController().stopRoute(kafkaRouteDef.getId());
	}

	@Test
	public void testKafkaConsumerCUDRoute() throws Exception {

		context.start();
		RouteDefinition kafkaRouteDef = context.getRouteDefinition("kafka-consumer-cud-route");
		configureRoute(kafkaRouteDef, "logger-cud");
		context.getRouteController().startRoute(kafkaRouteDef.getId());
		// assertErrorNotOccured();
		context.getRouteController().stopRoute(kafkaRouteDef.getId());
	}

	private void configureRoute(RouteDefinition routeDef, String endpoint) throws Exception{

		AdviceWith.adviceWith(context, routeDef.getId(), a -> {
			a.interceptSendToEndpoint("direct:" + endpoint).skipSendToOriginalEndpoint().to("direct:output");
		}
	);

	}

	private void assertErrorNotOccured() throws Exception{
		String body = consumerTemplate.receive("direct:output").getIn().getBody(String.class);
		assertNotEquals("errorOccured", body); 
	}

}
