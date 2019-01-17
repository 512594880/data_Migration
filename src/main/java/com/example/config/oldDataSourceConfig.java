package com.example.config;

/**
 * Created by wangxi on 2019/1/17.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryOld",
        transactionManagerRef = "transactionManagerOld",
        basePackages = {"com.example.repository"}) //设置Repository所在位置
@ComponentScan({"com.example.config"})
public class oldDataSourceConfig {

    @Autowired
    @Qualifier("oldDataBase")
    private DataSource oldDataSource;

    @Autowired
    private JpaProperties jpaProperties;

    @Primary
    @Bean(name = "entityManagerOld")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryOld(builder).getObject().createEntityManager();
    }

    @Primary
    @Bean(name = "entityManagerFactoryOld")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryOld(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(oldDataSource)
                .properties(getVendorProperties(oldDataSource))
                .packages("com.example.entity") //设置实体类所在位置
                .persistenceUnit("oldPersistenceUnit")
                .build();
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getProperties();
    }

    @Primary
    @Bean(name = "transactionManagerOld")
    public PlatformTransactionManager transactionManagerOld(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryOld(builder).getObject());
    }
}
