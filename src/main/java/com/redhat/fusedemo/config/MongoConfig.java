package com.redhat.fusedemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

@Configuration
public class MongoConfig {

	private MongoProperties mongoProperties;
	
	public MongoConfig(MongoProperties mongoProperties) {
		this.mongoProperties = mongoProperties;
	}

	@Bean("mongoBean")
	public MongoClient getMongoClient(){
		MongoClient mongoClient = null;		
		
		if(mongoProperties.getPort()!=null && mongoProperties.getPort()>0 
				&& mongoProperties.getHost()!=null && !mongoProperties.getHost().isEmpty()) {
			
			MongoCredential mongoCredential = MongoCredential.createCredential(
					mongoProperties.getUsername(), 
					mongoProperties.getDatabase(), 
					mongoProperties.getPassword().toCharArray()
			);
			
			MongoClientOptions options = MongoClientOptions.builder()
	                .connectionsPerHost(10)
	                .socketTimeout(10000)
	                .maxWaitTime(10000)
	                .connectTimeout(10000)
	                .build();
			
			ServerAddress serverAddresses = new ServerAddress(mongoProperties.getHost(), mongoProperties.getPort());
			
			System.out.println("Connection with " + mongoProperties.getHost() + ":" + mongoProperties.getPort() + " - " + serverAddresses.getPort());
			
			if(mongoProperties.getUsername().isEmpty()) 
				mongoClient = new MongoClient(serverAddresses);
			else
				mongoClient = new MongoClient(serverAddresses, mongoCredential, options);			
		}
		
		return mongoClient;
	}

}
