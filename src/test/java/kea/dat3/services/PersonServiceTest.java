package kea.dat3.services;

import kea.dat3.dto.PersonRequest;
import kea.dat3.dto.PersonResponse;
import kea.dat3.entities.Person;
import kea.dat3.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @Mock
    PersonRepository personRepository;
    PersonService personService;

    @BeforeEach
    void setupData() {
        personService = new PersonService(personRepository);
    }

    @Test
    void addPerson() {
        Person testPerson = new Person("m@test.mail","user","pass");
        Mockito.when(personRepository.save(any(Person.class))).thenReturn(testPerson);
        PersonResponse personRes = personService.addPerson(new PersonRequest(testPerson));
        assertEquals("user", personRes.getUsername());
    }

    @Test
    void getPersons() {
        Mockito.when(personRepository.findAll()).thenReturn(List.of(
                new Person("test1@mail.test","test1","doesitmatter"),
                new Person("test2@mail.test","test2","doesitmatter")
        ));
        List<PersonResponse> persons = personService.getPersons();
        assertEquals(2, persons.size());
    }

    @Test
    void getPerson() {
        Person p = new Person("test@mail.test","test","doesitmatter");
        Mockito.when(personRepository.findByUsername(p.getUsername())).thenReturn(Optional.of(p));
        PersonResponse personRes = personService.getPerson("test");
        assertEquals("test", personRes.getUsername());
    }
}