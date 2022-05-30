package kea.dat3.services;

import kea.dat3.dto.RiderRequest;
import kea.dat3.dto.RiderResponse;
import kea.dat3.entities.Rider;
import kea.dat3.entities.Team;
import kea.dat3.error.RiderNotFoundException;
import kea.dat3.error.TeamNotFoundException;
import kea.dat3.repositories.RiderRepository;
import kea.dat3.repositories.TeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiderService {

    private final RiderRepository riderRepository;
    private final TeamRepository teamRepository;

    public RiderService(RiderRepository riderRepository, TeamRepository teamRepository) {
        this.riderRepository = riderRepository;
        this.teamRepository = teamRepository;
    }

    public List<RiderResponse> findAll(Pageable pageable) {
        Page<Rider> riders = riderRepository.findAll(pageable);
        return riders.stream().map(RiderResponse::new).collect(Collectors.toList());
    }

    public RiderResponse findById(long id) {
        return new RiderResponse(riderRepository.findById(id).orElseThrow(() -> new RiderNotFoundException(id)));
    }

    public RiderResponse addRider(RiderRequest riderRequest) {
        long teamId = riderRequest.getTeamId();
        Team team = teamRepository.findById(teamId).orElseThrow(()->new TeamNotFoundException(teamId));
        Rider rider = new Rider(riderRequest);
        team.addRider(rider); // this also sets party in candidate
        teamRepository.save(team); // Cascades
        return new RiderResponse(rider);
    }

    public RiderResponse updateRider(long id, RiderRequest riderRequest) {
        Rider rider = riderRepository.findById(id).orElseThrow(() -> new RiderNotFoundException(id));
        rider.update(riderRequest);
        return new RiderResponse(riderRepository.save(rider));
    }

    public void deleteRider(long id) {
        Rider rider = riderRepository.findById(id).orElseThrow(()->new RiderNotFoundException(id));
        riderRepository.delete(rider);
    }
}
