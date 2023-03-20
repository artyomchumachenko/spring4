package ru.study.tasklist.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
@ConfigurationProperties("gitlab")
@ConstructorBinding
public record GitlabIntegrationProperties(
        @NotEmpty String url,
        @NotEmpty String token,

        @NotEmpty String projectId
) {
}
