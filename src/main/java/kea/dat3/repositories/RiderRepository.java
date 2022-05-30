package kea.dat3.repositories;

import kea.dat3.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {

    List<Rider> findByNameContainingIgnoreCase(String name);

}