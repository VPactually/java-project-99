package hexlet.code.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.taskStatuses.TaskStatusUpdateDTO;
import hexlet.code.model.TaskStatus;
import hexlet.code.repositories.TaskStatusRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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

    @BeforeEach
    public void setUp() {
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        taskStatusRepository.save(testTaskStatus);
    }

    @Test
    public void showTest() throws Exception {
        var response1 = mockMvc.perform(get("/api/task_statuses/1")
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertThat(response1.getContentAsString()).contains("Draft");
        assertThat(response1.getContentAsString()).contains("draft");
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
    public void createTest() throws Exception {
        var newStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();

        mockMvc.perform(post("/api/task_statuses")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(newStatus)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var status = taskStatusRepository.findBySlug(newStatus.getSlug()).orElseThrow();
        assertThat(status.getName()).isEqualTo(newStatus.getName());
        assertThat(status.getSlug()).isEqualTo(newStatus.getSlug());
    }

    @Test
    public void updateTest() throws Exception {
        var dto = new TaskStatusUpdateDTO("New Name", "new_slug");
        mockMvc.perform(put("/api/task_statuses/1")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        var taskStatus = taskStatusRepository.findBySlug(dto.getSlug().get()).orElseThrow();
        assertThat(taskStatus.getSlug()).isEqualTo(dto.getSlug().get());
        assertThat(taskStatus.getName()).isEqualTo(dto.getName().get());
    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc.perform(delete("/api/task_statuses/" + testTaskStatus.getId())
                        .with(token))
                .andExpect(status().isNoContent())
                .andReturn().getResponse();
        assertFalse(taskStatusRepository.findBySlug(testTaskStatus.getSlug()).isPresent());
    }

    @Test
    public void unauthorizedTest() throws Exception {
        mockMvc.perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put("/api/task_statuses/" + testTaskStatus.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/task_statuses"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/task_statuses/" + testTaskStatus.getId()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/api/task_statuses/" + testTaskStatus.getId()))
                .andExpect(status().isUnauthorized());
    }
}
