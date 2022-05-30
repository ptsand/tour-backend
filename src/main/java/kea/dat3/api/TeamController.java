package kea.dat3.api;

import kea.dat3.dto.RiderResponse;
import kea.dat3.dto.TeamRequest;
import kea.dat3.dto.TeamResponse;
import kea.dat3.services.TeamService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public List<TeamResponse> getTeams(Pageable pageable){
        return teamService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public TeamResponse getTeam(@PathVariable long id) {
        return teamService.findById(id);
    }

    @GetMapping("/{id}/riders")
    public List<RiderResponse> getTeamRiders(@PathVariable long id) {
        return teamService.getRiders(id);
    }
    @RolesAllowed({"USER","ADMIN"})
    @PostMapping
    public TeamResponse addTeam(@RequestBody @Valid TeamRequest teamRequest) {
        return teamService.addTeam(teamRequest);
    }
    @RolesAllowed({"USER","ADMIN"})
    @PutMapping("/{id}")
    public TeamResponse updateTeam(@PathVariable long id, @RequestBody @Valid TeamRequest teamRequest) {
        return teamService.updateTeam(id, teamRequest);
    }
    @RolesAllowed({"USER","ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok("{}");
    }
}
