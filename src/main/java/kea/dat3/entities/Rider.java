package kea.dat3.entities;

import kea.dat3.dto.RiderRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Rider {

    // TODO: check constraints

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String country;

    private int mountainPoints;
    private int sprintPoints;
    private int totalTimeMs;

    @ManyToOne
    private Team team;

    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime updated;

    public Rider(RiderRequest riderRequest) {
        this.name = riderRequest.getName();
        this.country = riderRequest.getCountry();
        this.totalTimeMs = riderRequest.getTotalTimeMs();
    }

    public void update(RiderRequest riderRequest) {
        this.name = riderRequest.getName();
    }
}
