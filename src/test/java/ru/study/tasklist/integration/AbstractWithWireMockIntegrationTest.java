package ru.study.tasklist.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.ServerSocket;


/**
 * Интеграционный тест приложения с зависимостями по HTTP, которые мокаются через WireMock
 * один WireMock на все зависимости. если приложение имеет
 */
public abstract class AbstractWithWireMockIntegrationTest extends AbstractIntegrationTest {
    static int wireMockPort;
    static {
        try (ServerSocket socket = new ServerSocket(0)) {
            wireMockPort =  socket.getLocalPort();
            log.info("Select port for wiremock is {}", wireMockPort);
            System.setProperty("MOCK_PORT", "" + wireMockPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Для мок для любых вызовов (ЕПА, карточки ЮЛ/ФЛ) - достаточно одного, просто разные интеграции будут идти на разные урлы */
    protected WireMockServer wireMockServer;

    @BeforeEach
    void configureWireMock() throws IOException {
        wireMockServer = new WireMockServer(new WireMockConfiguration().port(wireMockPort));
        wireMockServer.start();
        log.info("Staring wiremock on port {}", wireMockServer.port());
    }

    @AfterEach
    void stopWireMock() {
        wireMockServer.stop();
    }

}
