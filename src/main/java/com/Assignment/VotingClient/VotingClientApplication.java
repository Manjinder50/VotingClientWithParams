package com.Assignment.VotingClient;

import com.Assignment.VotingClient.service.DataExtracter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class VotingClientApplication implements CommandLineRunner {

	@Autowired
	DataExtracter dataExtracter;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(VotingClientApplication.class);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {

		dataExtracter.extractData(args[0]);
	}
}