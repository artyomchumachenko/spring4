package ru.study.tasklist.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@EnableJdbcAuditing
@Configuration
public class DatabaseConfig {
}
