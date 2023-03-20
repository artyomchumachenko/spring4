package ru.study.tasklist.service.integration.gitlab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.study.tasklist.config.properties.GitlabIntegrationProperties;
import ru.study.tasklist.model.integration.gitlab.IssueNotFoundException;

import java.io.IOException;


@Component
@EnableConfigurationProperties(GitlabIntegrationProperties.class)
public class RestGitlabClient implements GitlabClient {
    private static final Logger log = LoggerFactory.getLogger(RestGitlabClient.class);
    private final GitlabIntegrationProperties properties;
    private final RestOperations restOperations;

    public RestGitlabClient(GitlabIntegrationProperties properties, RestTemplateBuilder builder) {
        this.properties = properties;
        restOperations = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(properties.url()))
                .interceptors((request, body, execution) -> {
                    request.getHeaders().add("PRIVATE-TOKEN", properties.token());
                    return execution.execute(request, body);
                }, (request, body, execution) -> {
                    log.info("[{}] {} try to request", request.getMethod(), request.getURI());
                    return execution.execute(request, body);
                })
                .errorHandler(new ResponseErrorHandler() {
                    @Override
                    public boolean hasError(ClientHttpResponse response) throws IOException {
                        return response.getRawStatusCode() >= 400;
                    }

                    @Override
                    public void handleError(ClientHttpResponse response) throws IOException {
                        if (response.getRawStatusCode() == 400) {
                            throw new IssueNotFoundException(response.getStatusText());
                        }
                        throw new IllegalStateException(response.getStatusText());
                    }
                })
                .build();
    }

    @Override
    public void addCommentToIssue(String id, String comment) {
        restOperations.exchange(RequestEntity
                    .post("/api/v4/projects/{projectId}/issues/{issueId}/discussions?note={comment}",
                            properties.projectId(), id, comment)
                    .build()
                , String.class);

    }
}
