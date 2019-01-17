package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
//@ComponentScan({"com.example.config"})
public class DataMigrationSpringApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DataMigrationSpringApplication.class, args);
	}


	@Override
	public void run(String... strings) throws Exception {

	}
}

