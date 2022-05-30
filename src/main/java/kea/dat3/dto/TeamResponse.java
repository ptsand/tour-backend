package kea.dat3.dto;

import kea.dat3.entities.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponse {

    private String name;
    public TeamResponse(Team team) {
        this.name = team.getName();
    }
}
