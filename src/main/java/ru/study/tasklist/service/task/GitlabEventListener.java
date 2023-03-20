package ru.study.tasklist.service.task;


import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.study.tasklist.model.domain.task.events.OkEvent;
import ru.study.tasklist.service.integration.gitlab.GitlabClient;

@Component
public class GitlabEventListener {
    private final  GitlabClient client;

    public GitlabEventListener(GitlabClient client) {
        this.client = client;
    }

    @EventListener
    public void onGitlab(OkEvent event) {

        client.addCommentToIssue(event.task().getIssue(), event.getOkSubTask().toString());

    }
}
