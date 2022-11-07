package com.example.notificationservice.Config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.inject.Inject;
import javax.sql.DataSource;


@Configuration
@PropertySource("classpath:application.properties")
public class LiquibaseConfig {

    @Inject
    private Environment environment;

    @Bean
    public DataSource dataSource1() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(environment.getRequiredProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getRequiredProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));

        return dataSource;
    }

    @Bean
    public DataSource dataSource2() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(environment.getRequiredProperty("spring.datasource.url1"));
        dataSource.setUsername(environment.getRequiredProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));

        return dataSource;
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setDataSource(dataSource1());
        liquibase.setChangeLog("classpath:db/changelog/changelog-master.xml");

        return liquibase;
    }

    @Bean
    public SpringLiquibase liquibase2() {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setDataSource(dataSource2());
        liquibase.setChangeLog("classpath:db/changelog/changelog-master.xml");

        return liquibase;
    }
}
