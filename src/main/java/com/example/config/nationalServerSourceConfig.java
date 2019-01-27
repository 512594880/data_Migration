package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
        entityManagerFactoryRef = "entityManagerFactoryServer",
        transactionManagerRef = "transactionManagerServer",
        basePackages = {"com.example.nationalServerRepository"}) //设置Repository所在位置
@ComponentScan({"com.example.config"})
public class nationalServerSourceConfig {
    @Autowired
    @Qualifier("nationalServer")
    private DataSource newDataSource;

    @Autowired
    private JpaProperties jpaProperties;


    @Bean(name = "entityManagerServer")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryNew(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryServer")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryNew(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(newDataSource)
                .properties(getVendorProperties())
                .packages("com.example.entityServer") //设置实体类所在位置
                .persistenceUnit("newPersistenceUnit")
                .build();
    }

    private Map<String, Object> getVendorProperties() {
        return jpaProperties.getHibernateProperties(new HibernateSettings());
    }


    @Bean(name = "transactionManagerServer")
    public PlatformTransactionManager transactionManagerNew(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryNew(builder).getObject());
    }
}
