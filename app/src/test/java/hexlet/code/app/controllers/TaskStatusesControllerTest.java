package hexlet.code.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repositories.TaskStatusRepository;
import hexlet.code.app.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    private JwtRequestPostProcessor token;

    private TaskStatus testTaskStatus;

    private User anotherUser;

    @BeforeEach
    public void setUp() {
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        anotherUser = Instancio.of(modelGenerator.getUserModel()).create();
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
    }

    @Test
    public void indexTest() throws Exception {
        var response = mockMvc.perform(get("/api/task_statuses")
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertThat(response.getContentAsString()).contains("Draft");
        assertThat(response.getContentAsString()).contains("to_review");
        assertThat(response.getContentAsString()).contains("to_be_fixed");
        assertThat(response.getContentAsString()).contains("ToPublish");
        assertThat(response.getContentAsString()).contains("Published");
    }

    @Test
    public void showTest() throws Exception {
        var response1 = mockMvc.perform(get("/api/task_statuses/1")
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertThat(response1.getContentAsString()).contains("Draft");
        assertThat(response1.getContentAsString()).contains("draft");

        var response2 = mockMvc.perform(get("/api/task_statuses/2")
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertThat(response2.getContentAsString()).contains("ToReview");
        assertThat(response2.getContentAsString()).contains("to_review");

        var response5 = mockMvc.perform(get("/api/task_statuses/5")
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertThat(response5.getContentAsString()).contains("Published");
        assertThat(response5.getContentAsString()).contains("published");
    }

    @Test
    public void createTest() throws Exception {
        var data = Map.of("name", "Absolute Name", "slug", "absolute_slug");
        mockMvc.perform(post("/api/task_statuses")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data)))
                .andExpect(status().isCreated())
                .andReturn().getResponse();
        var response = mockMvc.perform(get("/api/task_statuses")
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).contains("Absolute Name");
        assertThat(response.getContentAsString()).contains("absolute_slug");
    }

    @Test
    public void updateTest() throws Exception {
        assertTrue(taskStatusRepository.findBySlug("draft").isPresent());
        var data = Map.of("name", "New Name", "slug", "new_slug");
        mockMvc.perform(put("/api/task_statuses/1")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data)))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var response = mockMvc.perform(get("/api/task_statuses/1")
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).contains("New Name");
        assertThat(response.getContentAsString()).contains("new_slug").doesNotContain("Draft", "draft");
    }

    @Test
    public void deleteTest() throws Exception {
        taskStatusRepository.save(testTaskStatus);

        mockMvc.perform(delete("/api/task_statuses/" + testTaskStatus.getId())
                        .with(token))
                .andExpect(status().isNoContent())
                .andReturn().getResponse();
        assertFalse(taskStatusRepository.findBySlug(testTaskStatus.getSlug()).isPresent());
    }

    @Test
    public void testCreateDeletePutTaskStatusWithoutAuth() throws Exception {
        taskStatusRepository.save(testTaskStatus);

        mockMvc.perform(post("/api/task_statuses")
                        .content(om.writeValueAsString(Map.of("name", "Fake name"))))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put("/api/task_statuses/" + testTaskStatus.getId())
                        .content(om.writeValueAsString(Map.of("name", "Fake name"))))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/api/task_statuses/" + testTaskStatus.getId()))
                .andExpect(status().isUnauthorized());
    }
}
