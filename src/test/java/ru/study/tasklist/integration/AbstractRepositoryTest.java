package ru.study.tasklist.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;

import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

/**
 * Интеграционный тест для проверки работы слоя DAO
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers(disabledWithoutDocker = true)
@DataJdbcTest
@Rollback
public abstract class AbstractRepositoryTest extends AbstractDatabaseTest {


    protected NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    protected DataSource dataSource;


    @BeforeEach
    void setUp() {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


}
