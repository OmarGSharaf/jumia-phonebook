package com.jumia.exercise.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class SQLiteConfig {

    final Environment env;

    public SQLiteConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(env.getProperty("driverClassName"))
                .url(env.getProperty("url"))
                .type(SQLiteDatasource.class)
                .build();
    }

    @Configuration
    @PropertySource("classpath:persistence.properties")
    static class SqliteConfig {

    }
}