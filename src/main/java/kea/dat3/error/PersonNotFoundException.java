package kea.dat3.error;

import org.springframework.http.HttpStatus;

public class PersonNotFoundException extends Client4xxException {

    private static final String message = "Person with username '%s' not found";

    public PersonNotFoundException(String username) {
        super(String.format(message, username), HttpStatus.NOT_FOUND);
    }

}
