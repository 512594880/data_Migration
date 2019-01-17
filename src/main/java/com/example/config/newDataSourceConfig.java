package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by wangxi on 2019/1/17.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryNew",
        transactionManagerRef = "transactionManagerNew",
        basePackages = {"com.example.newrepository"}) //设置Repository所在位置
@ComponentScan({"com.example.config"})
public class newDataSourceConfig {

    @Autowired
    @Qualifier("newDataBase")
    private DataSource newDataSource;

    @Autowired
    private JpaProperties jpaProperties;


    @Bean(name = "entityManagerNew")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryNew(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryNew")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryNew(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(newDataSource)
                .properties(getVendorProperties(newDataSource))
                .packages("com.example.newEntity") //设置实体类所在位置
                .persistenceUnit("newPersistenceUnit")
                .build();
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }


    @Bean(name = "transactionManagerNew")
    public PlatformTransactionManager transactionManagerNew(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryNew(builder).getObject());
    }

}
