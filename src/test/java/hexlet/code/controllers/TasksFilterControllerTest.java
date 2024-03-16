package hexlet.code.controllers;

import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repositories.LabelRepository;
import hexlet.code.repositories.TaskRepository;
import hexlet.code.repositories.TaskStatusRepository;
import hexlet.code.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TasksFilterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    private JwtRequestPostProcessor token;

    private Label bug;
    private Label feature;
    private TaskStatus draft;
    private TaskStatus review;
    private User admin;
    private User user;

    private Task task1;
    private Task task2;
    private Task task3;


    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        bug = labelRepository.findByName("bug").get();
        feature = labelRepository.findByName("feature").get();
        draft = taskStatusRepository.findBySlug("draft").get();
        review = taskStatusRepository.findBySlug("to_review").get();
        admin = userRepository.findByEmail("hexlet@example.com").get();
        user = userRepository.findByEmail("user@gmail.com").get();

        task1 = new Task();
        task2 = new Task();
        task3 = new Task();

        task1.setName("Task1");
        task1.setDescription("Desc1");
        task1.setTaskStatus(draft);
        task1.setAssignee(admin);
        task1.setLabels(Set.of(bug));
        taskRepository.save(task1);

        task2.setName("Task2");
        task2.setDescription("Desc2");
        task2.setTaskStatus(review);
        task2.setAssignee(user);
        task2.setLabels(Set.of(feature));
        taskRepository.save(task2);

        task3.setName("Task3");
        task3.setDescription("Desc3");
        task3.setTaskStatus(review);
        task3.setAssignee(admin);
        task3.setLabels(Set.of(feature));
        taskRepository.save(task3);
    }

    @Test
    public void withoutFilterTest() throws Exception {
        var responseWithoutFilter = mockMvc.perform(get("/api/tasks").with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(responseWithoutFilter).contains("Task1").contains("Task2").contains("Task3");
    }

    @Test
    public void titleContTest() throws Exception {
        var title1 = mockMvc.perform(get("/api/tasks?titleCont=sk2")
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(title1).doesNotContain(task1.getName()).doesNotContain(task3.getName()).contains(task2.getName());
        assertThat(title1).contains(task2.getDescription()).doesNotContain(task1.getDescription());

        var title2 = mockMvc.perform(get("/api/tasks?titleCont=4")
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(title2).isArray().isEmpty();
    }

    @Test
    public void assigneeTest() throws Exception {
        var assignee1 = mockMvc.perform(get("/api/tasks?assigneeId=" + admin.getId())
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(assignee1).contains("Task1").contains("Task3").doesNotContain("Task2");

        var assignee2 = mockMvc.perform(get("/api/tasks?assigneeId=" + user.getId())
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(assignee2).doesNotContain("Task1").doesNotContain("Task3").contains("Task2");
    }

    @Test
    public void statusTest() throws Exception {
        var status1 = mockMvc.perform(get("/api/tasks?status=" + draft.getSlug())
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(status1).contains(task1.getName()).doesNotContain(task2.getName()).doesNotContain(task3.getName());

        var status2 = mockMvc.perform(get("/api/tasks?status=" + review.getSlug())
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(status2).doesNotContain(task1.getName()).contains(task2.getName()).contains(task3.getName());

        var status3 = mockMvc.perform(get("/api/tasks?status=absolutely_new_slug")
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(status3).isArray().isEmpty();
    }

    @Test
    public void labelTest() throws Exception {
        var label1 = mockMvc.perform(get("/api/tasks?labelId=" + bug.getId())
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(label1).contains(task1.getName()).doesNotContain(task2.getName()).doesNotContain(task3.getName());

        var label2 = mockMvc.perform(get("/api/tasks?labelId=" + feature.getId())
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(label2).doesNotContain(task1.getName()).contains(task2.getName()).contains(task3.getName());

        var label3 = mockMvc.perform(get("/api/tasks?labelId=4")
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(label3).isArray().isEmpty();
    }

    @Test
    public void chainTest() throws Exception {

        var chain1 = mockMvc.perform(get("/api/tasks?titleCont=Task")
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(chain1).contains(task1.getName(), task2.getName(), task3.getName());

        var chain2 = mockMvc.perform(get("/api/tasks?titleCont=Task&assigneeId=" + admin.getId())
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(chain2).contains(task1.getName(), task3.getName()).doesNotContain(task2.getName());

        var chain3 = mockMvc.perform(get(
                "/api/tasks?titleCont=Task&assigneeId=" + admin.getId()
                        + "&status=" + draft.getSlug())
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(chain3).contains(task1.getName()).doesNotContain(task2.getName(), task3.getName());

        var chain4 = mockMvc.perform(get(
                        "/api/tasks?titleCont=Task&assigneeId=" + admin.getId()
                                + "&status=" + draft.getSlug() + "&labelId=" + bug.getId())
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(chain4).contains(task1.getName()).doesNotContain(task2.getName(), task3.getName());
    }
}
