package ru.study.tasklist.model.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.study.tasklist.model.dto.task.TaskResponse;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
public class SuperTest {
    @Autowired
    private ObjectMapper mapper;


    @Test
    void name() throws JsonProcessingException {
        mapper = new ObjectMapper();


        var response = mapper.readValue("""
                {
                    "name": "данные"
                }""", TaskResponse.class);


        assertThat(response)
            .hasFieldOrPropertyWithValue("name", "данные");

    }
}
