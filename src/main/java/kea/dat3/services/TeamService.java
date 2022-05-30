package kea.dat3.services;

import kea.dat3.dto.RiderResponse;
import kea.dat3.dto.TeamRequest;
import kea.dat3.dto.TeamResponse;
import kea.dat3.entities.Team;
import kea.dat3.error.TeamNotFoundException;
import kea.dat3.repositories.TeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<TeamResponse> findAll(Pageable pageable) {
        Page<Team> teams = teamRepository.findAll(pageable);
        return teams.stream().map(TeamResponse::new).collect(Collectors.toList());
    }

    public TeamResponse findById(long id) {
        return new TeamResponse(teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException(id)));
    }

    public TeamResponse addTeam(TeamRequest teamRequest) {
        Team team = teamRepository.save(new Team(teamRequest));
        return new TeamResponse(team);
    }

    public TeamResponse updateTeam(long id, TeamRequest teamRequest) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException(id));
        team.update(teamRequest);
        return new TeamResponse(teamRepository.save(team));
    }

    public void deleteTeam(long id) {
        Team team = teamRepository.findById(id).orElseThrow(()->new TeamNotFoundException(id));
        teamRepository.delete(team);
    }

    public List<RiderResponse> getRiders(long id) {
        Team team = teamRepository.findById(id).orElseThrow(()->new TeamNotFoundException(id));
        return team.getRiders().stream().map(RiderResponse::new).collect(Collectors.toList());
    }
}
