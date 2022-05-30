package kea.dat3.api;

import kea.dat3.dto.PersonRequest;
import kea.dat3.dto.PersonResponse;
import kea.dat3.error.UnauthorizedException;
import kea.dat3.services.PersonService;
import org.springframework.http.ResponseEntity;
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
    public List<PersonResponse> getPersons(HttpServletRequest request){
        if (!request.isUserInRole("ADMIN")) throw new UnauthorizedException();
        return personService.getPersons();
    }

    private void checkPrivileges(String username, HttpServletRequest request) {
        // Restrict access to the current user if not an ADMIN
        if(request.getUserPrincipal() == null) throw new UnauthorizedException();
        if(request.isUserInRole("ADMIN")) return;
        // users are allowed to request their own info
        if (request.getUserPrincipal().getName().equals(username)) return;
        throw new UnauthorizedException();
    }

    @GetMapping("/{username}")
    public PersonResponse getPerson(@PathVariable String username, HttpServletRequest request)  {
        checkPrivileges(username, request);
        return personService.getPerson(username);
    }

    @PutMapping("/{username}")
    public PersonResponse editPerson(@RequestBody PersonRequest body, @PathVariable String username, HttpServletRequest request){
        checkPrivileges(username, request);
        return personService.editPerson(body, username);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deletePerson(@PathVariable String username, HttpServletRequest request){
        checkPrivileges(username, request);
        personService.deletePerson(username);
        return ResponseEntity.ok("{}");
    }
}
