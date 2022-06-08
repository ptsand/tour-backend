package kea.dat3.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kea.dat3.entities.Person;
import kea.dat3.entities.Role;
import kea.dat3.repositories.PersonRepository;
import kea.dat3.security.dto.LoginRequest;
import kea.dat3.security.dto.LoginResponse;
import kea.dat3.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class PersonControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonService personService;
    @Autowired
    ObjectMapper objectMapper;

    Map<Role, LoginRequest> loginReqs = new HashMap<>();

    @BeforeEach
    public void setupPersonControllerTest() {
        personRepository.deleteAll();
        loginReqs.put(Role.USER, new LoginRequest("user","userPassword"));
        loginReqs.put(Role.ADMIN, new LoginRequest("admin","adminPassword"));
        Person user = new Person("email@testUser.dk", loginReqs.get(Role.USER));
        Person admin = new Person("email@testAdmin.dk", loginReqs.get(Role.ADMIN));
        user.addRole(Role.USER);
        admin.addRole(Role.ADMIN);
        personRepository.saveAll(List.of(user, admin));
    }

    @ParameterizedTest
    @EnumSource(Role.class)
    void testPersonFound(Role role) throws Exception {
        // admin and user should be allowed to request their user info
        LoginRequest request = loginReqs.get(role);
        String jwt = login(request);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/api/persons/%s", request.getUsername()))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(request.getUsername()));
    }

    @Test
    void testUserCannotGetAdminInfo() throws Exception {
        LoginRequest request = loginReqs.get(Role.USER);
        String jwt = login(request);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/api/persons/%s","admin"))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + jwt))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAdminHasAccess() throws Exception {
        LoginRequest request = loginReqs.get(Role.ADMIN);
        String jwt = login(request);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/api/persons/%s","user"))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + jwt))
                .andExpect(status().isOk());
    }

    @Test
    void testPersonNotFound() throws Exception {
        // request a nonexistent person and verify HTTP Status and error response
        String jwt = login(loginReqs.get(Role.ADMIN));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/persons/xxx")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + jwt))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Person with username 'xxx' not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("404"));
    }

    private String login(LoginRequest credentials) throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(credentials)))
                    .andReturn();
        if (response.getResponse().getStatus() == 401) {
            throw new Exception("Failed to login");
        }
        return objectMapper.readValue(response.getResponse().getContentAsString(), LoginResponse.class).getToken();
    }
}