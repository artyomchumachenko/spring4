package ru.study.tasklist.service.integration.gitlab;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.study.tasklist.model.integration.gitlab.IssueNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(RestGitlabClient.class)

// Можно выключить моки, тогда RestTemplate полезет туда, что указано в настройках. Так можно более эффективно отлаживать
// решение на реальном сервисе, либо просто выключить MockRestServiceServer в пользу какого-нибудь wiremock
//@AutoConfigureMockRestServiceServer(enabled = false)

// Можно использовать настройки из профиля, лично мне нравится прописать их тут рядом, чтобы все данные теста было в одном месте. В принципе, правильно и так, и так.
//@ActiveProfiles("test")
@TestPropertySource(properties = {
        "gitlab.token=glpat-99999999999999999999",
        "gitlab.url=https://gitlab.com",
        "gitlab.project-id=123123"
})


class RestGitlabClientTest {

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private RestGitlabClient client;

    @Test
    void testTrue() {
        mockServer.expect(requestTo("https://gitlab.com/api/v4/projects/123123/issues/123/discussions?note=test"))
                .andExpect(method(POST))
                .andRespond(
                        withSuccess(/* если результат мега большой, можно вынести в файл, например, так: new ClassPathResource("/response/test.json") */
                    """
                    test
                    """, MediaType.APPLICATION_JSON)
                );

        client.addCommentToIssue("123", "test");

    }

    @Test
    void testInvalid() {
        mockServer.expect(requestTo("https://gitlab.com/api/v4/projects/123123/issues/123/discussions?note=test"))
                .andExpect(method(POST))
                .andRespond(
                        withBadRequest()
                );

        var exception = catchThrowable(() ->client.addCommentToIssue("123", "test"));

        assertThat(exception).isInstanceOf(IssueNotFoundException.class);
    }

}