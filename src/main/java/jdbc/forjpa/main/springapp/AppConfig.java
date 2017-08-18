package jdbc.forjpa.main.springapp;
/*
 * Copyright (c) 2016 Ad.net. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "jdbc.forjpa.*")
@PropertySource(value = {"classpath:app.properties"})
public class AppConfig {

    @Autowired
    private Environment env;

    @Bean(name = "coreDataSource")
    @Primary
    @ConfigurationProperties(prefix = "jdbc")
    public DataSource getCoreDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(env.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(env.getRequiredProperty("jdbc.password"));
        return dataSource;
    }

    @Bean(name = "coreJdbcTemplate")
    public NamedParameterJdbcTemplate getBizJdbcTemplate() {
        return new NamedParameterJdbcTemplate(getCoreDataSource());
    }
}
