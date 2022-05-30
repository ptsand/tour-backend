package kea.dat3.config;

import kea.dat3.dto.RiderRequest;
import kea.dat3.dto.TeamRequest;
import kea.dat3.entities.*;
import kea.dat3.repositories.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import java.util.ArrayList;
import java.util.List;

@Controller
@Profile("!test")
public class MakeTestData implements ApplicationRunner {

    PersonRepository personRepository;
    TeamRepository teamRepository;

    public MakeTestData(PersonRepository personRepository, TeamRepository teamRepository) {
        this.personRepository = personRepository;
        this.teamRepository = teamRepository;
    }

    public void makeUsers() {
        Person user = new Person("user@mail.test","user","test12!");
        Person admin = new Person("admin@mail.test","admin","test12");
        Person user_admin = new Person("user-admin@mail.test","user_admin","test12");
        user.addRole(Role.USER);
        admin.addRole(Role.ADMIN);
        user_admin.addRole(Role.USER);
        user_admin.addRole(Role.ADMIN);
        personRepository.save(user);
        personRepository.save(admin);
        personRepository.save(user_admin);
        System.out.println("CREATED " + personRepository.count() + " TEST PERSONS");
    }

    private List<Team> makeTeams() {
        List<Team> teams = new ArrayList<>();
        for (int i = 2; i<6; i++) {
            teams.add(new Team(new TeamRequest("Team"+i)));
        }
        return teams;
    }

    private void makeRiders(List<Team> teams) {
        teams.stream().forEach(team ->{
            for (int i = 1; i<5; i++) {
                team.addRider(new Rider(new RiderRequest(team.getId(),"Name"+i,"country"+i,i*10000)));
            }
        });
        teamRepository.saveAll(teams);
    }

    @Override
    public void run(ApplicationArguments args) {
        makeUsers();
        makeRiders(makeTeams());
    }
}