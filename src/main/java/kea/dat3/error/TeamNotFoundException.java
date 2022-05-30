package kea.dat3.error;

import org.springframework.http.HttpStatus;

public class TeamNotFoundException extends Client4xxException {

    private static final String message = "Team with id '%s' not found";

    public TeamNotFoundException(long id) {
        super(String.format(message, id), HttpStatus.NOT_FOUND);
    }

}
