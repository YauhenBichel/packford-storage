package com.ybichel.storage.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class LiquibaseConfiguration {
    @Value("${spring.liquibase.change-log}")
    private String changeLogPath;
    @Value("${spring.liquibase.url}")
    private String url;
    @Value("${spring.liquibase.user}")
    private String user;
    @Value("${spring.liquibase.password}")
    private String password;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setDataSource(getDataSource());
        liquibase.setChangeLog(changeLogPath);

        return liquibase;
    }

    private DriverManagerDataSource getDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        return dataSource;
    }
}
