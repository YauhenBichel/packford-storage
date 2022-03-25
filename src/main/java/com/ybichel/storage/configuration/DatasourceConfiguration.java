package com.ybichel.storage.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfiguration {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Bean
    public DataSource datasource() {
        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .username(user)
                .password(password)
                .build();
    }
}

