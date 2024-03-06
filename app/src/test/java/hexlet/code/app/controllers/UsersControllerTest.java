package hexlet.code.app.controllers;

import hexlet.code.app.mappers.UserMapper;
import hexlet.code.app.model.User;
import hexlet.code.app.repositories.UserRepository;
import hexlet.code.app.services.UserService;
import hexlet.code.app.util.ModelGenerator;
import hexlet.code.app.utils.JWTUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


import static org.assertj.core.api.Assertions.assertThat;


import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
class UsersControllerTest {

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
    }

    @Test
    public void testIndex() throws Exception {
        userRepository.save(testUser);
        var result = mockMvc.perform(get("/users").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {

        userRepository.save(testUser);

        var request = get("/users/{id}", testUser.getId()).with(jwt());
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
    public void testCreate() throws Exception {

        var request = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testUser));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(testUser.getEmail()).get();

        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(user.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(user.getPasswordDigest()).isNotEqualTo(testUser.getPasswordDigest());
    }

    @Test
    public void testIndexWithoutAuth() throws Exception {
        userRepository.save(testUser);
        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testShowWithoutAuth() throws Exception {

        userRepository.save(testUser);

        var request = get("/users/{id}", testUser.getId());
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized());

    }


    @Test
    public void testDeleteAnotherUser() throws Exception {
        userRepository.save(testUser);
        userRepository.save(anotherUser);

        var token = jwt().jwt(builder -> builder.subject(anotherUser.getEmail()));

        var request = delete("/users/{id}", testUser.getId()).with(token);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());

    }

    @Test
    public void testUpdateAnotherUser() throws Exception {
        userRepository.save(testUser);
        userRepository.save(anotherUser);

        var token = jwt().jwt(builder -> builder.subject(anotherUser.getEmail()));
        var update = new HashMap<String, String>();
        update.put("firstName", "New First Name");
        update.put("lastName", "New Last Name");

        var request = put("/users/{id}", testUser.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(update));

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteUser() throws Exception {
        userRepository.save(testUser);

        var delReq = delete("/users/" + testUser.getId())
                .with(jwt().jwt(builder -> builder.subject(testUser.getEmail())));

        mockMvc.perform(delReq)
                .andExpect(status().isNoContent());
        assertFalse(userRepository.findByEmail(testUser.getEmail()).isPresent());
    }

    @Test
    public void testUpdateUser() throws Exception {
        userRepository.save(testUser);

        var token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
        var update = new HashMap<String, String>();
        update.put("firstName", "New First Name");
        update.put("lastName", "New Last Name");

        var updateReq = put("/users/" + testUser.getId())
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

}
