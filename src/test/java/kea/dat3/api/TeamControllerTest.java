package kea.dat3.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kea.dat3.dto.TeamRequest;
import kea.dat3.entities.Person;
import kea.dat3.entities.Role;
import kea.dat3.entities.Team;
import kea.dat3.repositories.PersonRepository;
import kea.dat3.repositories.TeamRepository;
import kea.dat3.security.dto.LoginRequest;
import kea.dat3.security.dto.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TeamControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    ObjectMapper objectMapper;
    Team t;
    long nonExistentId = 999;
    LoginRequest loginRequest;

    @BeforeEach
    public void setupTeamControllerTest() {
        teamRepository.deleteAll();
        personRepository.deleteAll();
        loginRequest = new LoginRequest("user","userPassword");
        Person user = new Person("email@testUser.dk", loginRequest);
        user.addRole(Role.USER);
        personRepository.save(user);
        t = new Team(new TeamRequest("Team"));
        teamRepository.save(t);
    }

    @Test
    public void testAddTeam() throws Exception {
        TeamRequest teamRequest = new TeamRequest("newTeam");
        String jwt = login(loginRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/teams")
                        .contentType("application/json")
                        .accept("application/json")
                        .header("Authorization","Bearer " + jwt)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        // Verify the screening ended in the right place
        assertEquals(2, teamRepository.count());
    }

    @Test
    public void testTeamNotFound() throws Exception {
        // request a nonexistent screening and verify HTTP Status and error response
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/teams/" + nonExistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Team with id '" + nonExistentId + "' not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("404"));
    }

    @Test
    public void testDeleteNonexistentTeam() throws Exception {
        String jwt = login(loginRequest);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/teams/" + nonExistentId)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + jwt))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Team with id '" + nonExistentId + "' not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("404"));

        assertEquals(1, teamRepository.count());
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


