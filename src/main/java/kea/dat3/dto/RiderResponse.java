package kea.dat3.dto;

import kea.dat3.entities.Rider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiderResponse {

    private long id;
    private String name;
    private String country;
    private String teamName;
    private long teamId;
    private int totalTimeMs;
    private int mountainPoints;
    private int sprintPoints;

    public RiderResponse(Rider rider) {
        this.id = rider.getId();
        this.name = rider.getName();
        this.country = rider.getCountry();
        this.teamName = rider.getTeam().getName();
        this.teamId = rider.getTeam().getId();
        this.totalTimeMs = rider.getTotalTimeMs();
        this.mountainPoints = rider.getMountainPoints();
        this.sprintPoints = rider.getSprintPoints();
    }
}
