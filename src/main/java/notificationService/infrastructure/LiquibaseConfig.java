package notificationService.infrastructure;

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

    @Inject
    private DataSource dataSource;

//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//
//        dataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"));
//        dataSource.setUrl(environment.getRequiredProperty("spring.datasource.url"));
//        dataSource.setUsername(environment.getRequiredProperty("spring.datasource.username"));
//        dataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));
//
//        return dataSource;
//    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(environment.getRequiredProperty("spring.liquibase.change-log"));

        return liquibase;
    }
}
