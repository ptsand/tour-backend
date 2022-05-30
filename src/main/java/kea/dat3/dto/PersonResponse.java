package kea.dat3.dto;

import kea.dat3.entities.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class PersonResponse {
    String username;
    List<String> roleNames;
    String email;

    String phoneNumber;

    public PersonResponse(Person person) {
        this.username = person.getUsername();
        this.roleNames = person.getRoles().stream().map(role -> role.toString()).collect(Collectors.toList());
        this.email = person.getEmail();
    }

    public static List<PersonResponse> getPersonsFromEntities(List<Person> persons) {
        return persons.stream().map(PersonResponse::new).collect(Collectors.toList());
    }
}
