package kea.dat3.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kea.dat3.dto.RiderRequest;
import kea.dat3.dto.TeamRequest;
import kea.dat3.entities.Person;
import kea.dat3.entities.Rider;
import kea.dat3.entities.Role;
import kea.dat3.entities.Team;
import kea.dat3.repositories.PersonRepository;
import kea.dat3.repositories.RiderRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class RiderControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    RiderRepository riderRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    ObjectMapper objectMapper;
    Rider r;
    Team t;
    long nonExistentId = 9999;

    LoginRequest loginRequest;

    @BeforeEach
    public void setupRiderControllerTest() {
        teamRepository.deleteAll(); // cascades to riders
        personRepository.deleteAll();
        loginRequest = new LoginRequest("user","userPassword");
        Person user = new Person("email@testUser.dk", loginRequest);
        user.addRole(Role.USER);
        personRepository.save(user);
        t = teamRepository.save(new Team(new TeamRequest("team easy on")));
        r = new Rider(new RiderRequest(t.getId(), "Bobby", "Denmark",34500000));
        t.addRider(r); // this sets team in rider too
        teamRepository.save(t); // cascades
    }

    @Test
    public void testAddRider() throws Exception {
        RiderRequest riderRequest = new RiderRequest(t.getId(),"newRider","Denmark",4234234);
        String jwt = login(loginRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/riders")
                        .contentType("application/json")
                        .accept("application/json")
                        .header("Authorization","Bearer " + jwt)
                        .content(objectMapper.writeValueAsString(riderRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        // Verify the screening ended in the right place
        assertEquals(2, riderRepository.count());
    }

    @Test
    public void testRiderNotFound() throws Exception {
        // request a nonexistent screening and verify HTTP Status and error response
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/riders/" + nonExistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Rider with id '" + nonExistentId + "' not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("404"));
    }

    @Test
    public void testDeleteNonexistentRider() throws Exception {
        String jwt = login(loginRequest);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/riders/" + nonExistentId)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + jwt))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Rider with id '" + nonExistentId + "' not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("404"));

        assertEquals(1, riderRepository.count());
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


