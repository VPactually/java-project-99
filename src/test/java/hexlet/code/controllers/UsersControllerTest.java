package hexlet.code.controllers;

import hexlet.code.dto.users.UserUpdateDTO;
import hexlet.code.model.User;
import hexlet.code.repositories.UserRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


import static org.assertj.core.api.Assertions.assertThat;


import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    private User testUser;

    private User anotherUser;

    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        anotherUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(testUser);
        userRepository.save(anotherUser);
    }

    @Test
    public void testShow() throws Exception {



        var request = get("/api/users/{id}", testUser.getId()).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                v -> v.node("lastName").isEqualTo(testUser.getLastName()),
                v -> v.node("email").isEqualTo(testUser.getEmail()),
                v -> v.node("id").isEqualTo(testUser.getId())
        );
    }

    @Test
    public void testIndex() throws Exception {

        var result = mockMvc.perform(get("/api/users").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testCreate() throws Exception {
        var newUser = Instancio.of(modelGenerator.getUserModel()).create();

        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newUser));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(newUser.getEmail()).get();

        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo(newUser.getFirstName());
        assertThat(user.getLastName()).isEqualTo(newUser.getLastName());
        assertThat(user.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(user.getPasswordDigest()).isNotEqualTo(newUser.getPasswordDigest());
    }

    @Test
    public void testUpdate() throws Exception {
        var token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
        var update = new UserUpdateDTO(
                "New First Name", "New Last Name");

        var updateReq = put("/api/users/" + testUser.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(update));

        mockMvc.perform(updateReq)
                .andExpect(status().isOk());

        var user = userRepository.findByEmail(testUser.getEmail()).get();

        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo("New First Name");
        assertThat(user.getLastName()).isEqualTo("New Last Name");
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    public void testUpdateAnotherUser() throws Exception {
        var token = jwt().jwt(builder -> builder.subject(anotherUser.getEmail()));
        var update = new UserUpdateDTO(
                "New First Name", "New Last Name");
        var request = put("/api/users/{id}", testUser.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(update));

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDelete() throws Exception {
        var delReq = delete("/api/users/" + testUser.getId())
                .with(jwt().jwt(builder -> builder.subject(testUser.getEmail())));

        mockMvc.perform(delReq)
                .andExpect(status().isNoContent());
        assertFalse(userRepository.findByEmail(testUser.getEmail()).isPresent());
    }

    @Test
    public void testDeleteAnotherUser() throws Exception {
        var token = jwt().jwt(builder -> builder.subject(anotherUser.getEmail()));
        var request = delete("/api/users/{id}", testUser.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isForbidden());

    }

}
