package kea.dat3.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kea.dat3.dto.TeamRequest;
import kea.dat3.entities.Team;
import kea.dat3.repositories.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
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
    ObjectMapper objectMapper;
    Team t;
    long nonExistentId = 999;

    @BeforeEach
    public void setupTeamControllerTest() {
        teamRepository.deleteAll();
        t = new Team(new TeamRequest("Team"));
        teamRepository.save(t);
    }

    @Test
    public void testAddTeam() throws Exception {
        TeamRequest teamRequest = new TeamRequest("newTeam");
        // Check that server answer with msg and statusCode
        mockMvc.perform(MockMvcRequestBuilders.post("/api/teams")
                        .contentType("application/json")
                        .accept("application/json")
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
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/teams/" + nonExistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Team with id '" + nonExistentId + "' not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("404"));

        assertEquals(1, teamRepository.count());
    }
}


