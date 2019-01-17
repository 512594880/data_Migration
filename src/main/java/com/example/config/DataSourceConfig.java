package com.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "oldDataBase")
    @Qualifier("oldDataBase")
    @ConfigurationProperties(prefix="spring.datasource.oldDataBase")
    public DataSource oldDataBase() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "newDataBase")
    @Qualifier("newDataBase")
    @Primary
    @ConfigurationProperties(prefix="spring.datasource.newDataBase")
    public DataSource newDataBase() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "oldDataBase")
    public JdbcTemplate primaryJdbcTemplate(
            @Qualifier("oldDataBase") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "newDataBase")
    public JdbcTemplate secondaryJdbcTemplate(
            @Qualifier("newDataBase") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
