package com.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
//        return DataSourceBuilder.create().build();
        return primaryDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean(name = "primaryDataSourceProperties")
    @Qualifier("primaryDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.old")
    public DataSourceProperties primaryDataSourceProperties() {
        System.out.println("111111111111");
        return new DataSourceProperties();
    }


    @Bean(name = "newDataBase")
    @Qualifier("newDataBase")
    @ConfigurationProperties(prefix = "spring.datasource.new")
    public DataSource newDataSource() {
        log.info("======newDataSource=======");
//        return DataSourceBuilder.create().build();
        return secondaryDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "nationalServer")
    @Qualifier("nationalServer")
    @ConfigurationProperties(prefix = "spring.datasource.server")
    public DataSource nationNalServer() {
//        log.info("======newDataSource=======");
//        return DataSourceBuilder.create().build();
        return thirdDataSourceProperties().initializeDataSourceBuilder().build();
    }


    @Bean(name = "secondaryDataSourceProperties")
    @Qualifier("secondaryDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.new")
    public DataSourceProperties secondaryDataSourceProperties() {
        System.out.println("222222222222222222");
        return new DataSourceProperties();
    }

    @Bean(name = "thirdDataSourceProperties")
    @Qualifier("thirdDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.server")
    public DataSourceProperties thirdDataSourceProperties() {
        System.out.println("3333333");
        return new DataSourceProperties();
    }




}