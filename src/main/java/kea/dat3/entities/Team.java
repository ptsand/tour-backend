package kea.dat3.entities;

import kea.dat3.dto.TeamRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Rider> riders = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime updated;

    public Team(TeamRequest teamRequest) {
        this.name = teamRequest.getName();
    }

    public void update(TeamRequest teamRequest) {
        this.name = teamRequest.getName();
    }

    public void addRider(Rider rider){
        this.riders.add(rider);
        rider.setTeam(this);
    }

    public void removeRider(Rider rider) {
        this.riders.remove(rider);
    }
}
