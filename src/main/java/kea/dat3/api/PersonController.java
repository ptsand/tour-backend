package kea.dat3.api;

import kea.dat3.dto.PersonRequest;
import kea.dat3.dto.PersonResponse;
import kea.dat3.services.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/persons")
public class PersonController {

    PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<PersonResponse> addPerson(@RequestBody @Valid PersonRequest body) {
        return ResponseEntity.ok(personService.addPerson(body));
    }

    @RolesAllowed("ADMIN")
    @GetMapping
    public List<PersonResponse> getPersons(){
        return personService.getPersons();
    }
    // Use method security expressions for more fine-grained control
    @PreAuthorize("#username == principal.username or hasRole('ADMIN')")
    @GetMapping("/{username}")
    public PersonResponse getPerson(@PathVariable String username, HttpServletRequest request)  {
        return personService.getPerson(username);
    }

    @PreAuthorize("#username == principal.username or hasRole('ADMIN')")
    @PutMapping("/{username}")
    public PersonResponse editPerson(@RequestBody PersonRequest body, @PathVariable String username){
        return personService.editPerson(body, username);
    }

    @PreAuthorize("#username == principal.username or hasRole('ADMIN')")
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deletePerson(@PathVariable String username){
        personService.deletePerson(username);
        return ResponseEntity.ok("{}");
    }
}
