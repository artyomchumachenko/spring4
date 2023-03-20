package ru.study.tasklist.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import org.springframework.web.util.DefaultUriBuilderFactory;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Общий интеграционный тест - запускает всё приложение на случайном порте
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers(disabledWithoutDocker = true)
public abstract class AbstractIntegrationTest extends AbstractDatabaseTest {
    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:" + port));
    }

}
