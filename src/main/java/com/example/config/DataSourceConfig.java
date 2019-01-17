package com.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import javax.sql.DataSource;


@Configuration
@Slf4j
public class DataSourceConfig {

    @Bean(name = "oldDataBase")
    @Primary
    @Qualifier("oldDataBase")
    @ConfigurationProperties(prefix = "spring.datasource.old")
    public DataSource oldDataSource() {
        log.info("=========oldDataBase=========");
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "newDataBase")
    @Qualifier("newDataBase")
    @ConfigurationProperties(prefix = "spring.datasource.new")
    public DataSource newDataSource() {
        log.info("======newDataSource=======");
        return DataSourceBuilder.create().build();
    }
}