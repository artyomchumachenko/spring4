package ru.study.tasklist.service.integration.gitlab;

public interface GitlabClient {

    void addCommentToIssue(String id, String comment);
}
