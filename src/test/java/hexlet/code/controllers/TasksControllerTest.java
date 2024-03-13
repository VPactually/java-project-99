package hexlet.code.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.tasks.TaskCreateDTO;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repositories.LabelRepository;
import hexlet.code.repositories.TaskRepository;
import hexlet.code.repositories.TaskStatusRepository;
import hexlet.code.repositories.UserRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class TasksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    private JwtRequestPostProcessor token;

    private Task testTask;
    private TaskStatus testTaskStatus;
    private User testUser;

    @BeforeEach
    public void setUp() {
        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

        taskStatusRepository.save(testTaskStatus);
        userRepository.save(testUser);

        testTask.setTaskStatus(testTaskStatus);
        testTask.setAssignee(testUser);
    }


    @Test
    public void indexTest() throws Exception {
        var response = mockMvc.perform(get("/api/tasks")
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(response).isArray();

        taskRepository.save(testTask);

        var response2 = mockMvc.perform(get("/api/tasks")
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(response2)
                .contains(String.valueOf(testTask.getAssignee().getId()))
                .contains(testTask.getName())
                .contains(testTask.getDescription())
                .contains(testTask.getTaskStatus().getSlug());
    }

    @Test
    public void showTest() throws Exception {

        taskRepository.save(testTask);

        var response = mockMvc.perform(get("/api/tasks/" + testTask.getId())
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("assignee_id").isEqualTo(testUser.getId()),
                v -> v.node("status").isEqualTo(testTaskStatus.getSlug())
        );

        mockMvc.perform(get("/api/tasks/" + testTask.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void createTest() throws Exception {
        assertFalse(taskRepository.findByName(testTask.getName()).isPresent());

        var dto = new TaskCreateDTO();
        dto.setTitle(testTask.getName());
        dto.setContent(testTask.getDescription());
        dto.setIndex(testTask.getIndex());
        dto.setAssigneeId(testTask.getAssignee().getId());
        dto.setStatus(testTask.getTaskStatus().getSlug());

        var response = mockMvc.perform(post("/api/tasks")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(response).and(
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("content").isEqualTo(testTask.getDescription()),
                v -> v.node("status").isEqualTo(testTask.getTaskStatus().getSlug()),
                v -> v.node("assignee_id").isEqualTo(testTask.getAssignee().getId())
        );

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());

        var dto2 = new TaskCreateDTO();
        dto2.setTitle(testTask.getName() + "new");
        dto2.setContent(testTask.getDescription() + "new");
        dto2.setIndex(testTask.getIndex() + testTask.getIndex());
        dto2.setStatus(testTask.getTaskStatus().getSlug());
        dto2.setAssigneeId(null);
        dto2.getTaskLabelIds().add(1L);
        dto2.getTaskLabelIds().add(2L);

        var response2 = mockMvc.perform(post("/api/tasks")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto2)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(response2).and(
                v -> v.node("taskLabelIds").isEqualTo("[1,2]")
        );
    }

    @Test
    public void updateTest() throws Exception {
        taskRepository.save(testTask);
        assertTrue(taskStatusRepository.findBySlug("to_review").isPresent());

        var dto = new HashMap<>();
        dto.put("title", "New Title");
        dto.put("content", "New Content");
        dto.put("status", "to_review");

        var response = mockMvc.perform(put("/api/tasks/" + testTask.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        assertThatJson(response).and(
                v -> v.node("title").isEqualTo("New Title"),
                v -> v.node("content").isEqualTo("New Content"),
                v -> v.node("status").isEqualTo("to_review")
        );

        mockMvc.perform(put("/api/tasks/" + testTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteTest() throws Exception {
        taskRepository.save(testTask);

        mockMvc.perform(delete("/api/tasks/" + testTask.getId()))
                .andExpect(status().isUnauthorized());


        mockMvc.perform(delete("/api/tasks/" + testTask.getId())
                        .with(token))
                .andExpect(status().isNoContent())
                .andReturn().getResponse();
        assertFalse(taskRepository.findByName(testTask.getName()).isPresent());
    }

}
